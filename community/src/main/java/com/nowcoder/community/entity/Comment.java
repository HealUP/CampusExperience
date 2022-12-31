package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* Description: 评论表实体类
* date: 2022/12/31 21:50
 * 
* @author: Deng
* @since JDK 1.8
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;

}
