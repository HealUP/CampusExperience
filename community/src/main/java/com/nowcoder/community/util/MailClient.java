package com.nowcoder.community.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
* Description: 邮件客户端
* date: 2022/12/24 16:47
 *
* @author: Deng
* @since JDK 1.8
*/

@Slf4j
@Configuration
public class MailClient {
    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")//注入值
    private String from;//来自哪里


    /**发送邮件方法定义
     *
     * @param to 发送到哪
     * @param subject 邮件的标题
     * @param content 邮件的内容
     */

    public void sendMail(String to,String subject,String content){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);//允许发送html模板的内容
        } catch (MessagingException e) {
            log.error("发送邮件失败："+e.getMessage());//捕获了异常后打error级别的日志
            e.printStackTrace();
        }

    }
}
