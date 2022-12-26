package com.nowcoder.community.controller;

import com.nowcoder.community.util.CommunityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
* Description: 测试控制器
* date: 2022/12/22 15:00
 *
* @author: Deng
* @since JDK 1.8
*/
@Controller
@ApiOperation("测试的接口")
@RequestMapping("/alpha")
public class AlaController {
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "牛客第一天！";
    }
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        //获取请求数据 服务端能得到请求的数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name+":"+value);
        }
        System.out.println(request.getParameter("code"));
        //返回相应数据
        response.setContentType("text/html;charset=utf-8");
        try (
                PrintWriter writer = response.getWriter();
                ){
           writer.write("<h1>牛客<h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    get请求 获取资源的两种方式
    // /student?current=1&limit=20
   @RequestMapping(path = "/students",method = RequestMethod.GET)
   @ResponseBody
    public String Students(
            @RequestParam(name = "current",required = false,defaultValue = "1") int current,
            @RequestParam(name = "limit",required = false,defaultValue = "10") int limit){
       System.out.println(current);
       System.out.println(limit);
       return "some students";
   }

// /student/id
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }
//post请求 提交资源的两种方式
    //1 前端访问模板文件下的html文件，提交表单action为（/community/alpha/student）后跳转到该控制器
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return "sucess";
    }
    //2 响应HTML数据 两种方式
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","牛客");
        modelAndView.addObject("age","30");
        modelAndView.setViewName("/demo/view");//templates默认带html后缀了
        return modelAndView;
    }

    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){//默认情况下，表达式从Model中获取数据。
        model.addAttribute("name","暨南大学");
        model.addAttribute("age",90);
        //返回一个路径
        return "demo/view";

    }
    //响应JSON异步请求 注册时输入用户名，页面不刷新，但是发送了请求查询服务器是否存在该用户名
    //Java对象> json > js对象
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","dzh");
        emp.put("age",20);
        emp.put("salary",20000.00);
        return emp;
    }
    //返回包含map元素的list
    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","dzh");
        emp.put("age",25);
        emp.put("salary",20000.00);
        list.add(emp);


        emp = new HashMap<>();
        emp.put("name","dxy");
        emp.put("age",24);
        emp.put("salary",20500.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","dxm");
        emp.put("age",27);
        emp.put("salary",20100.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","dgr");
        emp.put("age",45);
        emp.put("salary",20100.00);
        list.add(emp);
        return list;
    }

    /**
    * Description: 会话管理——cookie示例  浏览器访问服务器，服务器返回一个cookie给浏览器(响应cookie)
     * 浏览器把cookie存到内存中，如果关闭浏览器，那么cookie就会消失；另一种情况是，如果服务端设置了cookie的生命周期，cookie就会存在
     * 客户端的硬盘里，到时间就失效；服务器设置cookie指定的生效路径
     * 浏览器带着cookie去访问指定的路径，可以看到请求头中包含了cookie，而服务端可以把cookie打印出来
     * 弊端：不安全，容易泄露隐私，如账号密码等
    * date: 2022/12/25 11:21
     *
    * @author: Deng
    * @since JDK 1.8
    */

    @RequestMapping(path = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());//UUID随机生成cookie
        //设置cookie的生效范围
        cookie.setPath("/community/alpha");
        //设置cookie的生存时间
        cookie.setMaxAge(600 * 10);//10分钟
        //发送cookie
        response.addCookie(cookie);

        return "set cookie";
    }
    
    /**
    * Description: 浏览器携带cookie来
    * date: 2022/12/25 11:35
     * 
    * @author: Deng
    * @since JDK 1.8
    */

    @RequestMapping(path = "/cookie/get",method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get cookie";
    }

    /**
    * Description: session示例  JAVAEE 标准
     * 流程：浏览器访问服务器，服务器生成一个session,由springMVC框架自动注入生成，服务器将sessionId放到cookie中，放回给浏览器
     * 浏览器可以看到cookie中保存的JSESSIONID
     * 弊端：增加服务器的压力，可以使用JWT认证方式，但是会比较耗时，牺牲时间换空间
     * 好处：数据存放在服务端更加安全
     * 分布式部署服务器，使用session的弊端
     * 弊端：每一次通过nginx分发的负载均衡不能保证分配到获取到sessionid的服务器
     * 解决方案
     *   -  1粘性session，绑定号sessionid对应的服务器，每次来session,对应访问之前的服务器，那负载就不均衡了
     *   -  2同步session，很多台服务器同步获取sessionid，产生耦合，加大服务器压力
     *   -  3共享session，专门存session,单体的，如果挂了，那么就出问题了
     *   - 最佳解决：
     *     - 存cookie,存到mysql数据库集群，但是性能慢
     *     - 存到redis,性能快
    * date: 2022/12/25 11:51
     *
    * @author: Deng
    * @since JDK 1.8
    */

    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session) {
        session.setAttribute("id", 1);
        session.setAttribute("name", "Test");
        return "set session";
    }

    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }

    // ajax示例
    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0, "操作成功!");
    }
}
