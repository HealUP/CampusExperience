package com.nowcoder.community.service;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
* Description: 实现业务
* date: 2022/12/23 14:53
 *
* @author: Deng
* @since JDK 1.8
*/


@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

//    @Autowired
//    private LoginTicketMapper loginTicketMapper;

    /**
    * Description: 优先从缓存中取出用户
    * date: 2023/1/5 15:35
     *
    * @author: Deng
    * @since JDK 1.8
    */
    public User findUserById(int id) {
//        return userMapper.selectById(id);
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    public Map<String,Object> register(User user) {
        //实例化map
        HashMap<String, Object> map = new HashMap<>();

        //空值处理
        if (user == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("usernameMsg", "密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("usernameMsg","邮箱不能为空！");
            return map;
        }


        //验证账号

        User u = userMapper.selectByName(user.getUsername());
        if (u != null){
            map.put("usernameMsg","账号已存在！");
            return map;
        }

        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null){
            map.put("emailMsg","该邮箱已被注册!");
            return map;
        }

        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));//设置密码：MD5加密用户设置的密码+随机生成的5位数的盐值
        user.setType(0);//普通用户
        user.setStatus(0);//正常
        user.setActivationCode(CommunityUtil.generateUUID());//随机生成code
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));//产生0-999范围内的随机数
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //激活邮件
        Context context = new Context();//thymeleaf包下的
        context.setVariable("email",user.getEmail());

        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();//拼接路径
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);//发送邮件  内容去controller实现发送模板

        return map;//此时map为空

    }


    /**
    * Description: 激活
    * date: 2022/12/25 10:05
     *
    * @author: Deng
    * @since JDK 1.8
    */

    public int activation(int userId,String code){
        //找到用户
        User user = userMapper.selectById(userId);

        //判断用户状态status，是否激活
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        }else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId, 1);
            //修改了user,就要把缓存清理掉
            clearCache(userId);
            return ACTIVATION_SUCCESS;//跟激活码相等，则激活成功
        }else {
            return ACTIVATION_FAILURE;
        }

    }

    /**
    * Description: 登录判断
    * date: 2022/12/26 20:09
     *
    * @author: Deng
    * @since JDK 1.8
    */
    public Map<String, Object> login(String username, String password, int expiredSeconds){

        Map<String, Object> map = new HashMap<>();
        //空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        //验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("userMsg","该账号不存在！");
            return map;
        }

        //验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg","该账号未激活！");
            return map;
        }
        //验证密码
        password = CommunityUtil.md5(password + user.getSalt());//获取盐值加上密码，比对加密后的密码
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg","密码不正确！");
        }


        //生成登录凭证

        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
//        loginTicketMapper.insertLoginTicket(loginTicket);
        String redisKey = RedisKeyUtil.getTicketKey((loginTicket.getTicket()));
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    /**
    * Description: 登出方法
    * date: 2022/12/26 22:07
     *
    * @author: Deng
    * @since JDK 1.8
    */

    public void logout(String ticket) {
//        loginTicketMapper.updateStatus(ticket,1);//根据ticket，将状态改为失效 1
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);//得到对象
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }
    
    /**
    * Description: 根据ticket找到登录凭证LoginTicket  重构成从缓存中取
    * date: 2022/12/27 17:03
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    public LoginTicket findLoginTicket(String ticket) {
//        return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }


    /**
    * Description: 更新头像地址
    * date: 2022/12/29 20:56
    * @author: Deng
    * @since JDK 1.8
    */

    public int updateHeader (int userId, String headerUrl) {
        //为了防止更新失败,但是把缓存清理了的情况  干脆先更新,成功了,再清理缓存
//       return userMapper.updateHeader(userId, headerUrl);
        int rows = userMapper.updateHeader(userId, headerUrl);
        clearCache(userId);
        return rows;
    }

    /**
    * Description: 修改密码方法
    * date: 2022/12/30 18:29
     *
    * @author: Deng
    * @since JDK 1.8
    */

    // 重置密码
    public Map<String, Object> resetPassword(String email, String password) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(email)) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证邮箱
        User user = userMapper.selectByEmail(email);
        if (user == null) {
            map.put("emailMsg", "该邮箱尚未注册!");
            return map;
        }

        // 重置密码
        password = CommunityUtil.md5(password + user.getSalt());
        userMapper.updatePassword(user.getId(), password);

        map.put("user", user);
        return map;
    }

    // 修改密码
    public Map<String, Object> updatePassword(int userId, String oldPassword, String newPassword) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(oldPassword)) {
            map.put("oldPasswordMsg", "原密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(newPassword)) {
            map.put("newPasswordMsg", "新密码不能为空!");
            return map;
        }

        // 验证原始密码
        User user = userMapper.selectById(userId);
        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
        if (!user.getPassword().equals(oldPassword)) {
            map.put("oldPasswordMsg", "原密码输入有误!");
            return map;
        }

        // 更新密码
        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        userMapper.updatePassword(userId, newPassword);

        return map;
    }

    /**
    * Description: 根据name查找user
    * date: 2023/1/2 21:20
     *
    * @author: Deng
    * @since JDK 1.8
    */

    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }
    
    /**
    * Description: 查询user优先从缓存里面找
    * date: 2023/1/5 15:28
     * 找不到时初始化缓存的数据
     * 当数据发生变化时把缓存删掉
    * @author: Deng
    * @since JDK 1.8
    */
    // 1.优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2.取不到时初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3.数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }
    
    /**
    * Description: 查询某个用户的权限
    * date: 2023/1/10 16:34
     * 
    * @author: Deng
    * @since JDK 1.8
    */

    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1 :
                        return AUTHORITY_ADMIN;//管理员
                    case 2 :
                        return AUTHORITY_MODERATOR;//版主
                    default:
                        return AUTHORITY_USER;//用户
                }
            }
        });
        return list;
    }


}
