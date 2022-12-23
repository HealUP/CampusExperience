package com.nowcoder.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import com.nowcoder.community.entity.User;

/**
* Description: Mapper接口
* date: 2022/12/22 21:33
 * 
* @author: Deng
* @since JDK 1.8
*/

@Mapper
public interface UserMapper  {
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id,int status);

    int updateHeader(int id,String headerUrl);

    int updatePassword(int id,String password);
}
