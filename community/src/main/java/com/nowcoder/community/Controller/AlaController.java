package com.nowcoder.community.Controller;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Pageable;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.*;

/**
* Description: 测试控制器
* date: 2022/12/22 15:00
 *
* @author: Deng
* @since JDK 1.8
*/
@Controller
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
    @RequestMapping(path = "teacher",method = RequestMethod.GET)
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

}
