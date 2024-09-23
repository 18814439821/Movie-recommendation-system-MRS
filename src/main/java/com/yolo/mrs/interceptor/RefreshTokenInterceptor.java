package com.yolo.mrs.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.yolo.mrs.model.VO.UsersVO;
import com.yolo.mrs.utils.UserHolder;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yolo.mrs.utils.RedisConstant.USER_LOGIN_KEY;
import static com.yolo.mrs.utils.RedisConstant.USER_LOGIN_TTL;

@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取token
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // 没有Authorization头部或格式不正确，直接放行
            return true;
        }

        // 提取token
        String token = authorization.substring("Bearer ".length()).trim();
        if (token.isEmpty()) {
            // token为空字符串，直接放行
            return true;
        }

        //根据token去redis查询
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + token);
        //不存在直接放行
        if (entries.isEmpty()) {
            return true;
        }
        //存在把用户数据存到ThreadLocal
        UsersVO user = BeanUtil.fillBeanWithMap(entries, new UsersVO(), false);
        UserHolder.setUser(user);
        //redis续期，并放行
        stringRedisTemplate.expire(USER_LOGIN_KEY + token, USER_LOGIN_TTL, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
