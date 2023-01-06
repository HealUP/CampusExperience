package com.nowcoder.community.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
@Slf4j


/**
* Description: aop实现统一记录日志
* date: 2023/1/3 15:33
 * 
* @author: Deng
* @since JDK 1.8
*/
public class ServiceLogAspect {

    //定义切点 要执行的位置* com.nowcoder.community.service.*.*(..))
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut() {
        //该定义的切点不写任何东西，只在注解处定义了要执行的位置

    }
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        //拼接格式 用户[1.2.3.4],在[xxx],访问了[com.nowcoder.community.service.xxx()].
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (attributes == null) {//消费者主动去调用的controller，没有request,所以要判断是否为空
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteHost();//获取ip
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        log.info(String.format("用户[%s],在[%s],访问了[%s].", ip, now, target));
    }


}
