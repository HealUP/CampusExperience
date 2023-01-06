package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.schema.ModelRef;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(path = "/user")
public class UserController implements CommunityConstant {

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Resource
    private LikeService likeService;

    @Resource
    private FollowService followService;

    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage( Model model) {

        return "/site/setting";
    }

    //上传图片
    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage,Model model) {
        //没选择图片
        if (headerImage == null) {
            model.addAttribute("error","您还没有选择图片！");
            return "/site/setting";//回到设置页面
        }

        String fileName = headerImage.getOriginalFilename();//获取文件的名字
        // 判断后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));//从文件名的最后一个.来取
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error","文件格式不正确！");
            return "/site/setting";
        }
        //生成随机文件名
       fileName = CommunityUtil.generateUUID()+suffix;
        //确定存放路径 d:/work/data/upload/fileName
        File dest = new File(uploadPath+"/"+fileName);

        //存储文件
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            log.error("上传文件出现了错误："+e.getMessage());//捕获错误到日志中
            throw new RuntimeException("上传文件失败，服务器发生异常！",e);
        }

        log.info("开始获取当前用户id");//bug在这! 这里的bug只能修改一次头像，下次就报空指针，改了拦截器里的模板引擎后执行的地方
        // http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();//从hostHolder获取到当前用户
        System.out.println(hostHolder.getUser().getId());



        //拼接路径
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        //根据用户id更新头像地址
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";

    }

    //查看用户头像
    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public void getHeader (@PathVariable("fileName") String fileName, HttpServletResponse response) {
        //解析出服务器存放路径
        fileName = uploadPath + "/" + fileName;
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("/image"+suffix);
        try (//放在这里会自动关闭
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);//长度为b
            }
        } catch (IOException e) {
            log.error("读取头像失败: " + e.getMessage());
        }
    }

    /**
    * Description: 修改密码
    * date: 2022/12/30 18:27
     *
    * @author: Deng
    * @since JDK 1.8
    */
    // 修改密码
    @RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(String oldPassword, String newPassword, Model model) {
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(user.getId(), oldPassword, newPassword);
        if (map == null || map.isEmpty()) {
            return "redirect:/logout";
        } else {
            model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
            model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
            return "/site/setting";
        }
    }

    /**
    * Description: 个人主页
    * date: 2023/1/4 21:04
     *
    * @author: Deng
    * @since JDK 1.8
    */

    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);

        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {//判断当前用户有没有登录
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }
}
