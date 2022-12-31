package com.nowcoder.community.service;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService implements CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private SensitiveFilter sensitiveFilter;
    
    /**
    * Description: 根据实体找到评论内容 对内容进行分页
    * date: 2022/12/31 22:11
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    public List<Comment> findCommentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

/**
* Description: 查找评论数量
* date: 2022/12/31 22:13
 * 
* @author: Deng
* @since JDK 1.8
*/
    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }
    
    /**
    * Description:添加评论
     * 开启事务管理 隔离级别为READ_COMMITTED propagation 为Propagation.REQUIRED
    * date: 2022/12/31 22:10
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        //添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);
        
        //更新帖子评论数量
        
        //...
        return rows;
    }
    
    /**
    * Description: 根据id查找评论
    * date: 2022/12/31 22:08
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    public Comment findCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }

}
