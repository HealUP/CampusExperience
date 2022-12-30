package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* Description: 自定义注解
* date: 2022/12/30 18:48
 *
* @author: Deng
* @since JDK 1.8
*/
/**
* Description: 标记在方法上，拦截器拦截有该注解的方法，同时会判断当前的用户登录状态，一次来确定能不能给用户访问
* date: 2022/12/30 19:16
 *
* @author: Deng
* @since JDK 1.8
*/
@Target(ElementType.METHOD)//作用在方法上
@Retention(RetentionPolicy.RUNTIME)//作用时间 运行时
public @interface LoginRequired {

}

