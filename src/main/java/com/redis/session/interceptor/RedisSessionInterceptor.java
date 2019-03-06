package com.redis.session.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Auther: xuzhoukai
 * @Date: 2019/3/5 15:20
 * @Description:
 */
public class RedisSessionInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession();
        String userId = (String) httpSession.getAttribute("user_id");
        if(userId != null){
            String sessionId = stringRedisTemplate.opsForValue().get(userId);
            if(httpSession.getId().equals(sessionId)){
                return true;
            }else{
                return false;
            }
        }else{
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print("用户未登录！");
            return false;
        }
    }

}
