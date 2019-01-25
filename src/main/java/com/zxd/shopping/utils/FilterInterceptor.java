package com.zxd.shopping.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;

/**
 * @author 张孝德
 * @Date 2019/1/23
 * 自定义拦截器，用以拦截接口token
 */

public class FilterInterceptor implements HandlerInterceptor {
    @Autowired
    RedisUtil redisUtil;
    //预处理
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        // 拦截token
        if(redisUtil.get("token") == null) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write(JSON.toJSONString(ResultUtil.error(2001,"token失效")));
            pw.flush();
            pw.close();
            return false;
        } else {
            redisUtil.set("token", redisUtil.get("token"));
            return true;
        }
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
