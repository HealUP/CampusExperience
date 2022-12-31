package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
* Description: 登录凭证实体类
* date: 2022/12/25 18:55
 * 
* @author: Deng
* @since JDK 1.8
*/
@Data
public class LoginTicket {

    private int id;

    private int userId;

    private String ticket;

    private int status;

    private Date expired;//失效时间
}
