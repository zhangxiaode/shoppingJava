package com.zxd.shopping.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

/**
 * @author 张孝德
 * @Date 2019/1/23
 * 自定义拦截器，用以拦截接口token
 */
public class FilterInterceptor implements HandlerInterceptor {
    //预处理
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        // 拦截token
//        System.out.println("111" + token);
        return true;
    }

    /**
     * 该方法将在Controller执行之后，返回视图之前执行，modelAndView表示请求Controller处理之后返回的Model和View对象，所以可以在
     * 这个方法中修改modelAndView的属性，从而达到改变返回的模型和视图的效果。
     */
    //后处理
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
    }

    //返回处理
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }

//    public String getRemortIP(HttpServletRequest request) {
//        if (request.getHeader("x-forwarded-for") == null) {
//            return request.getRemoteAddr();
//        }
//        return request.getHeader("x-forwarded-for");
//    }
}
