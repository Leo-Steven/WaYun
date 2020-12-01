package com.mentality.yun.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        // 1. 强转，将req 强转为 HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) req;
        // 2. 获取资源路径
        String uri = request.getRequestURI();
        // 3. 判断资源路径
        if (uri.contains("/login.html")||uri.contains("/checkCode")||uri.contains("/login")||
                uri.contains(".css")||uri.contains(".js")||uri.contains("/imgs/")){
            // 包含登录请求，则释放
            chain.doFilter(req, resp);
        }else{
            // 不包含，则验证用户是否已经登录
            Object user = request.getSession().getAttribute("current_user");
            if(user!=null){
                // 已经登录，释放
                chain.doFilter(req,resp);
            }else{
                request.getRequestDispatcher("/login.html").forward(request,resp);
            }
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}
