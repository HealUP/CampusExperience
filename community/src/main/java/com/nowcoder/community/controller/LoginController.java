package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Slf4j
@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;
//
//    @Autowired
//    private RedisTemplate redisTemplate;

    /**
    * Description: 点击注册，跳转到注册页面
    * date: 2022/12/25 10:26
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }
    
    /**
    * Description: 点击提交按钮，进行注册逻辑的判断
    * date: 2022/12/25 10:26
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()) {
            //如果map是空的，说明注册成功了
            model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了一封激活邮件,请尽快激活！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            //其他情况，map不为空，获取map的值
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    /**
    * Description: 从路径中取出userId和激活码，进行激活逻辑的判断
    * date: 2022/12/25 10:28
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    @RequestMapping(path = "/activaton/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg","激活成功，您的账号可以正常使用了！");
            model.addAttribute("target","/login");//跳转到登录页面
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg","无效操作，该账号已经激活过了！");
            model.addAttribute("target","/index");//跳转到首页
        } else {
            model.addAttribute("msg","激活失败，您提供的激活码不正确！");
            model.addAttribute("target","/index");//跳转到首页
        }
        return "/site/operate-result";
    }


/**
* Description: 跳转到登录页面
* date: 2022/12/25 10:40
 *
* @author: Deng
* @since JDK 1.8
*/


    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    /**
    * Description: 生成验证码
    * date: 2022/12/25 18:20
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);

        // 验证码的归属——存入cookie
        String kaptchaOwner = CommunityUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("响应验证码失败："+e.getMessage());
        }
    }
    

    /**
    * Description: 检查验证码
    * date: 2022/12/26 21:43
     * 逻辑：从session里面获取验证码kaptcha
    * @author: Deng
    * @since JDK 1.8
    */

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, HttpSession session, HttpServletResponse response) {
        //检查验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确！");
            return "/site/login";
        }

        //检查账号密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds) ;
        //判断返回的map中是否包含ticket key
        if (map.containsKey("ticket")) {
            //将ticket写入cookie
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);//生效的路径
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";//跳转到首页
        } else {
            //否则会有其他的错误提示消息，返回给model
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "site/login";//返回登录页
        }
    }
    /**
    * Description: 登出
    * date: 2022/12/26 22:06
     * 根据ticket修改用户激活状态即可
    * @author: Deng
    * @since JDK 1.8
    */

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }
}
