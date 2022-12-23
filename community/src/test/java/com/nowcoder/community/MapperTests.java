package com.nowcoder.community;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    UserMapper userMapper;
    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("小邓");
        user.setPassword("a123456");
        user.setSalt("abc");
        user.setEmail("test1@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/103.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testselectByName(){
        User user = userMapper.selectByName("admin");
        System.out.println(user);
    }

    @Test
    public void testselectByEmail(){
        User user = userMapper.selectByEmail("test2@qq.com");
        System.out.println(user);
    }
    @Test
    public void testselectById(){
        User user = userMapper.selectById(2);
        System.out.println(user);
    }

    @Test
    public void updateUser(){
        int rows = userMapper.updateStatus(4,1);
        System.out.println(rows);

        rows = userMapper.updateHeader(4,"http://www.nowcoder.com/105.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(4,"123456");
        System.out.println(rows);

    }

}
