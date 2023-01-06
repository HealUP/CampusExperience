package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Description: 一登陆 就跳到拦截器，从HostHolder 里面获取用户id,再从消息表里面查找，私信通知+系统通知的未读数量，计算出来后，显示到消息头部
* date: 2023/1/6 17:25
 *
* @author: Deng
* @since JDK 1.8
*/
@Component
public class MessageInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);//私信未读
            int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);//系统消息未读
            modelAndView.addObject("allUnreadCount", letterUnreadCount + noticeUnreadCount);//相加起来返回给模板
        }
    }

}
