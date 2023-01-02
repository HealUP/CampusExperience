package com.nowcoder.community.service;

import cn.hutool.http.HtmlUtil;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;
/**
* Description: 调用Mapper层 实现业务
* date: 2022/12/23 14:46
 *
* @author: Deng
* @since JDK 1.8
*/
@Service
public class DiscussPostService {

    @Autowired  //换成@Resource不会报警告
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPosts(int userId,int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    public  int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
    * Description: 发布帖子的方法
    * date: 2022/12/31 17:24
     *
    * @author: Deng
    * @since JDK 1.8
    */
    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        //转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPosts(post);

    }

    /**
    * Description: 根据id查找帖子
    * date: 2022/12/31 19:04
     *
    * @author: Deng
    * @since JDK 1.8
    */

    public DiscussPost findDiscussPostById(int id) {
       return discussPostMapper.selectDiscussPostById(id);
    }
    
    /**
    * Description: 更新帖子评论数量
    * date: 2023/1/2 17:09
     * 
    * @author: Deng
    * @since JDK 1.8
    */

    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }
}
