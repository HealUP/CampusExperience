package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    /**
    * Description: 发帖子前要判断是否是登录的用户
    * date: 2022/12/31 17:28
     *
    * @author: Deng
    * @since JDK 1.8
    */


    @RequestMapping(path = "/add",method = RequestMethod.POST)
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        //判断当前是否是登录的用户
        if (user == null) {
            return CommunityUtil.getJSONString(403,"您当前还没有登录哦！");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());//当前用户id
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());//当前时间为发布时间  后期可以改为自动填充时间策略
        discussPostService.addDiscussPost(discussPost);//插入

        //报错的情况，后面统一处理
        return CommunityUtil.getJSONString(0,"发布成功！");
    }

    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)

    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        //帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",post);
        //作者 从帖子里面获取作者id
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);

        //评论分页信息


        return "/site/discuss-detail";
    }

}
