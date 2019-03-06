package com.redis.session.controller;

import com.redis.session.constant.Constant;
import com.redis.session.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: xuzhoukai
 * @Date: 2019/3/5 14:45
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/first", method = RequestMethod.GET)
    public Map<String, Object> firstResp (HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        request.getSession().setAttribute("request Url地址", request.getRequestURL());
        map.put("request Url地址", request.getRequestURL());
        return map;
    }

    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public Object sessions (HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        map.put("session标准", request.getSession().getId());
        map.put("message开心", request.getSession().getAttribute("map"));
        return map;
    }
    @RequestMapping("/login")
    public String login(HttpServletRequest request, @RequestParam(name = "userName") String userName,
                        @RequestParam(name = "password")String password){
        String mes = request.getRequestURI();
        System.out.println(mes);
        if("admin".equals(userName)&&"123".equals(password)){
            HttpSession session = request.getSession();
            session.setAttribute("user_id",userName+":"+password);
            redisTemplate.opsForValue().set(userName+":"+password,session.getId(),10,TimeUnit.MINUTES);
            return "登录成功";
        }
        return "登录失败";
    }

    @RequestMapping("/hello")
    public String getInfo(){
        return "你好开心";
    }

    @RequestMapping("/exit")
    public String exit(HttpServletRequest request){
        request.getSession().invalidate();
        return "注销成功";
    }

}
