package com.nowcoder.community;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.mapper.CommentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;
    @Test
    public void insertCommentTest() {

        Comment comment = new Comment();
//        comment.setId(1);
        comment.setUserId(2);
        comment.setEntityType(1);
        comment.setEntityId(1);
        comment.setTargetId(1);
        comment.setContent("2022快结束了！");
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentMapper.insertComment(comment);
    }

}
