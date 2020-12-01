package com.mentality.yun.web.servlet;

import com.mentality.yun.domain.ResultInfo;
import com.mentality.yun.domain.User;
import com.mentality.yun.service.UserService;
import com.mentality.yun.service.impl.UserServiceImpl;
import com.mentality.yun.util.MailUtils;
import com.mentality.yun.util.UuidUtil;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet(name = "UserServlet", urlPatterns = "/user/*")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserServiceImpl();

    // 用户登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        User loginUser = new User();
        // 1. 获取前台数据
        Map<String, String[]> parameterMap = request.getParameterMap();
        try {
            BeanUtils.populate(loginUser, parameterMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // 1.1 首先获取验证码，并对验证码进行校验
        String code = request.getParameter("checkCode");
        String checkCode = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        // 获取完后将器删除
        request.getSession().invalidate();

        if (!(checkCode != null && checkCode.equalsIgnoreCase(code))) {
            // 如果验证码错误，则将错误信息返回
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误啦，");
        } else {
            // 验证码正确

            User user = userService.login(loginUser);
            if (user != null) {
                // 用户登录成功，判断是否激活
                if ("Y".equals(user.getStatus())) {
                    // 处于激活状态
                    resultInfo.setFlag(true);
                    // 将对象存入session
                    request.getSession().setAttribute("current_user", user);
                } else {
                    // 用户还未激活
                    resultInfo.setFlag(false);
                    resultInfo.setErrorMsg("小家伙，你账户还没激活呢~~");
                }

            } else {
                // 用户登录失败
                resultInfo.setErrorMsg("自习检查下用户名和密码哈");
                resultInfo.setFlag(false);
            }
        }
        // 将登录信息写回前台
        this.writeValue(response, resultInfo);
    }

    // 用户注册
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        // 添加验证码验证
        String code = request.getParameter("checkCode");
        String checkCode = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        // 验证码获取后将session对象删除
        request.getSession().invalidate();

        if (checkCode != null && checkCode.equalsIgnoreCase(code)) {
            // 验证码正确的情况
            User registUser = new User();
            Map<String, String[]> parameterMap = request.getParameterMap();
            try {
                BeanUtils.populate(registUser, parameterMap);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            String uCode = UuidUtil.getUuid();
            // 设置激活状态为 N
            registUser.setStatus("N");
            // 设置激活码
            registUser.setCode(uCode);
            boolean flag = userService.regist(registUser);

            // 已经在前天进行提供ajax异步请求查询用户名重复问题
            if (flag) {
                // 注册成功
                resultInfo.setFlag(true);
                // 邮件激活设置 ----> 发送文件，路径的书写
                String content = "<div style=\"text-align:center;red:yellow;font-weight:bold;height:150px;padding-top:100px;font-size:30px;\">\n" +
                        "    \t\t<h4>"+"恭喜你哈，注册成功了，<a href=\"http://localhost:8080/user/active?code=" +
                        uCode + "\"> 点我进行激活~ </a>"+"</h4>\n" +
                        "    \t</div>";
                MailUtils.sendMail(registUser.getEmail(), content, "万事胜意！");

            } else {
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("——注册失败——");
            }
        } else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
        }

        // 将resultInfo信息输出到前天
        this.writeValue(response, resultInfo);
    }

    // 用户激活
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msg;
        // 获取参数 激活码 code
        String code = request.getParameter("code");

        // 判断用户是否存在
        if (code != null && !("null".equals(code))) {
            // 将用户激活
            boolean flag = userService.active(code);

            // 判断激活成功进行页面跳转

            if (flag) {
                // 激活成功，注意页面跳转不要些绝对路径，否则部署到服务器就就会找不到对应的文件
                // 页面跳转
                msg = "激活成功了，放心以后我会常常给你发广告的~~~<a href=\"" + request.getContextPath() + "/login.html\">立即登录</a>";
            } else {
                // 激活码不正确
                msg = "是不是还没有注册吖~~，快来注册吧<a href=\"" + request.getContextPath() + "/regist.html\">立即注册~</a>";
            }
        } else {
            // 激活码为空
            msg = "是不是还没有注册吖~~，快来注册吧<a href=\"\">立即注册~</a>";
        }
        // 将结果输出到页面展示,
        // 设置编码以及文件格式
        response.setContentType("text/html;charset=utf-8");

        response.getWriter().write(msg);
    }

    // 获取用户信息
    public void userInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("current_user");
        this.writeValue(response, user);
    }

    // 用户退出
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 将用户session信息删除
        request.getSession().invalidate();
        // 跳转到首页或者登录页面
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    // 查询对应用户是否存在
    public void checkedUsername(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        // 获取参数
        String username = request.getParameter("username");

        // 调用service查询
        boolean flag = userService.findByUsername(username);

        // 封装ResultInfo 对象
        if (flag) {
            // 存在对应用户
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("当前用户名已被占用，请添加您独一无二的username把");
        } else {
            resultInfo.setFlag(true);
        }
        // 返回json数据
        this.writeValue(response, resultInfo);
    }

    // 判断 用户是否已经登陆
    public void hasLogin(HttpServletRequest request,HttpServletResponse response) throws IOException {
        Object current_user = request.getSession().getAttribute("current_user");
        ResultInfo resultInfo = new ResultInfo();
        if (current_user == null){
            resultInfo.setFlag(false);
        }else{
            resultInfo.setFlag(true);
        }
        this.writeValue(response,resultInfo);
    }

    // 开通会员
    public void becomeVip(HttpServletRequest request,HttpServletResponse response){
        // 首先获取用户id
        User user = (User) request.getSession().getAttribute("current_user");
        // 开通会员
        boolean flag = userService.becomeVip(user.getUid());
        if (flag){
            // 设置成功后需要将session对象更新
            try {
                int uid = ((User) request.getSession().getAttribute("current_user")).getUid();
                request.getSession().setAttribute("current_user",userService.updateUserInfo(uid));
                response.sendRedirect(request.getContextPath()+"vip.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            response.setContentType("text/html;charset=utf-8");
            try {
                response.getWriter().write("<h3>会员开通失败，请联系管理员进行开通</h3>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
