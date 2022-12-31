package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    //根据评论实体找到评论内容
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    //统计
    int selectCountByEntity(int entityType, int entityId);

    //添加评论
    int insertComment(Comment comment);

    //查看评论
    Comment selectCommentById(int id);
}
