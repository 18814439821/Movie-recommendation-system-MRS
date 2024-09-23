package com.yolo.mrs.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.yolo.mrs.model.DTO.UsersDTO;
import com.yolo.mrs.model.VO.UsersVO;
import com.yolo.mrs.utils.UserHolder;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

import static com.yolo.mrs.utils.RedisConstant.USER_LOGIN_KEY;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Object token = request.getHeader("Authorization");
        //尝试获取redis中是否存储当前对象
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + token);
        UsersVO usersVO = BeanUtil.fillBeanWithMap(entries, new UsersVO(), false);
        if (BeanUtil.isEmpty(usersVO)) {
            //查无此用户
            response.sendError(401,"用户未登录");
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
