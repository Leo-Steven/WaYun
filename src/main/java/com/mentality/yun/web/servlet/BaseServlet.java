package com.mentality.yun.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 通过抽取servlet进行方法的分发
 * <p>
 * 以及一些servlet中常用的方法
 * 1. json数据的转化以及输出
 * 2. json数据转化为字符串
 */
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取uri
        String uri = req.getRequestURI();

        // 2. 截取方法名
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);

        // 3. 通过反射调用方法
        Method method = null;
        try {
            method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将对象转化为json数据写回前台
     *
     * @param response
     * @param o
     * @throws IOException
     */
    public void writeValue(HttpServletResponse response, Object o) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(response.getWriter(), o);

    }

    /**
     * 将 传入的对象转化为json数据，并返回
     *
     * @param response
     * @param o
     * @return
     * @throws JsonProcessingException
     */
    public String writeValueAsString(HttpServletResponse response, Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(o);

    }
}
