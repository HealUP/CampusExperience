package com.nowcoder.community.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
* Description: user表实体类
* date: 2022/12/22 21:25
 *
* @author: Deng
* @since JDK 1.8
*/
@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String salt;//盐值
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;



}
