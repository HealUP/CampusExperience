package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.util.CommunityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest
//@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    DiscussPostMapper discussPostMapper;
    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("小hao");
        user.setPassword("a123456");
        user.setSalt("abc");
        user.setEmail("test1@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/106.png");
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

    /**
    * Description: 测试DiscussPostMapper接口
    * date: 2022/12/23 13:01
     *
    * @author: Deng
    * @since JDK 1.8
    */

    @Test
    public void  testSelectPosts(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(2, 0, 11);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }
        int rows = discussPostMapper.selectDiscussPostRows(2);
        System.out.println(rows);

    }

    //添加帖子
    @Test
    public void insertPosts(){
        int count =100;
        int score =50;
        int addCount = 0;
        for (int i = 20; i < 30; i++) {
            DiscussPost discussPost = new DiscussPost();
            discussPost.setContent("如何干掉牛客网"+i);
            discussPost.setCommentCount(count--);
            discussPost.setScore(score--);
            discussPost.setStatus(2);
            discussPost.setType(1);
            discussPost.setTitle("掌握牛客网的黑客攻防术"+i);
            discussPost.setUserId(4);
            discussPost.setCreateTime(new Date());
            discussPostMapper.insertDiscussPosts(discussPost);
            addCount ++;
        }
        System.out.println(addCount);
    }

    /**
    * Description: 测试LoginTicketMapper
    * date: 2022/12/26 21:16
     *
    * @author: Deng
    * @since JDK 1.8
    */
    @Test
    public void testLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(9);
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date());
        loginTicket.setTicket("cc");
        loginTicketMapper.insertLoginTicket(loginTicket);


        loginTicketMapper.updateStatus("aa",1);
    }

    /**
    * Description: 测试私信
    * date: 2023/1/2 19:51
     *
    * @author: Deng
    * @since JDK 1.8
    */
    @Test
    public void testSelectLetters() {}


    /**
    * Description: 测试插入私信
    * date: 2023/1/2 19:52
     *
    * @author: Deng
    * @since JDK 1.8
    */

    @Test
    public int testInsertLetters() {
        return 1;
    }

}
