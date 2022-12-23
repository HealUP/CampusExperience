package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId,int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    public  int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
