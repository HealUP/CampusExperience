package com.nowcoder.community.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
* Description: 帖子实体类
* date: 2022/12/31 21:50
 * 
* @author: Deng
* @since JDK 1.8
*/
@Data
@Document(indexName = "discusspost", shards = 6, replicas = 3)
public class DiscussPost {
    //每一个字段和es的索引对应起来了
    @Id
    private int id ;

    @Field(type = FieldType.Integer)
    private int userId;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")//双引号里面是分词器的名字
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")//双引号里面是分词器的名字
    private String content;

    @Field(type = FieldType.Integer)
    private int type;

    @Field(type = FieldType.Integer)
    private int status;

    @Field(type = FieldType.Date)
    private Date createTime;

    @Field(type = FieldType.Integer)
    private int commentCount;

    @Field(type = FieldType.Double)
    private double score;
}
