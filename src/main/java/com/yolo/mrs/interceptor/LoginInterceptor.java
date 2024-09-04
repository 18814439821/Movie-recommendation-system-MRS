package com.yolo.mrs.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.yolo.mrs.model.DTO.UsersDTO;
import com.yolo.mrs.utils.UserHolder;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.yolo.mrs.utils.RedisConstant.USER_LOGIN_KEY;

public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取当前登录用户
        UsersDTO user = UserHolder.getUser();
        Object token = request.getSession().getAttribute("authorization");
        //尝试获取redis中是否存储当前对象
        UsersDTO usersDTO = (UsersDTO) stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + token);
        if (BeanUtil.isEmpty(usersDTO)) {
            //查无此用户
            response.sendError(401,"用户未登录");
            return false;
        }else if (user != usersDTO){
            //非当前登录用户
            return false;
        }
        return !BeanUtil.isEmpty(user);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
