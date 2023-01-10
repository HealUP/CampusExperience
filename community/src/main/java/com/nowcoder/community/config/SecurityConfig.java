package com.nowcoder.community.config;

import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");//忽略拦截静态资源
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //让以下路径授权才能访问
        http.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"
                )
                .hasAnyAuthority(//授权给用户，管理员，版主，
                        AUTHORITY_USER,
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful"
                )
                .hasAnyAuthority(
                        AUTHORITY_MODERATOR//只有版主才能置顶和加精
                )
                .antMatchers(
                        "/discuss/delete",
                        "/data/**",
                        "/actuator/**"
                )
                .hasAnyAuthority(//只有管理员才能删贴
                        AUTHORITY_ADMIN
                )
                .anyRequest().permitAll()//其他的路径都允许
                .and().csrf().disable();//关闭csrf检查  如果开启的话，要在所有涉及到异步请求的地方，即提交表单，在相应的html的代码中添加关于csrf的token生成，还有在js中的ajax中添加

        //权限不够的时候的处理
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    //没有登录的情况下
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        //判断当前请求是同步还是异步，通过判断消息头的值来判断是同步还是异步
                        String xRequestWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestWith)) {
                            //如果是异步请求，就返回JSON格式字符串
                            response.setContentType("application/plain;charset=utf-8");//设置响应的格式
                            //获取到流
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403, "你还没有登录哦！"));
                        } else {
                            //如果是同步请求，重定向到登录页面
                            response.sendRedirect(request.getContextPath() + "/login");
                        }
                    }
                })
                //权限不足的时候
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        String xRequestWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestWith)) {
                            //如果是异步请求，就返回JSON格式字符串
                            response.setContentType("application/plain;charset=utf-8");//设置响应的格式
                            //获取到流
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403, "你没有访问此功能的权限！"));
                        } else {
                            //如果是同步请求，重定向到错误页面
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        // Security底层默认会拦截/logout请求,进行退出处理. 底层是filter,会默认拦截我们的logout的路径
        // 覆盖它默认的逻辑,才能执行我们自己的退出代码.

        http.logout().logoutUrl("/securitylogout");//放一个不存在的路径，欺骗security
    }
 }

