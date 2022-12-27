//package com.nowcoder.community.controller.interceptor;
//
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
//* Description: 测试拦截器
//* date: 2022/12/27 16:35
// *
//* @author: Deng
//* @since JDK 1.8
//*/
//@Component
//@Slf4j
//public class AlphaInterceptor implements HandlerInterceptor {
//    //controller 之前执行
//    @Override
//    public boolean preHandle( HttpServletRequest request,  HttpServletResponse response, Object handler) throws Exception {
//        log.debug("preHandle: " + handler.toString());
//        return true;
//    }
//
//    // 在Controller之后执行
//    @Override
//    public void postHandle( HttpServletRequest request,  HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//        log.debug("postHandle: " + handler.toString());
//    }
//
//    // 在TemplateEngine之后执行
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        log.debug("afterCompletion: " + handler.toString());
//    }
//}
