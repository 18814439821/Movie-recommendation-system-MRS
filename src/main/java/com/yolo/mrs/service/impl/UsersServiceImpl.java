package com.yolo.mrs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import com.yolo.mrs.model.DTO.LoginFormDTO;
import com.yolo.mrs.model.PO.Users;
import com.yolo.mrs.mapper.UsersMapper;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.yolo.mrs.utils.RedisConstant.USER_LOGIN_KEY;
import static com.yolo.mrs.utils.RedisConstant.USER_LOGIN_TTL;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yolo
 * @since 2024-08-29
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result login(LoginFormDTO loginFormDTO){
        String username = loginFormDTO.getUsername();
        String password = loginFormDTO.getPassword();
        String phone = loginFormDTO.getPhone();
        //判断用户是否存在
        Users user = lambdaQuery().eq(Users::getUsername, username).one();
        //用户名不为空并且查到了该用户
        if (ObjectUtil.isNotEmpty(user)){
            //判断密码是否错误
            if (!Objects.equals(user.getPassword(), password)){
                return Result.fail("用户名，手机号或密码错误");
            }else {
                String token = UUID.randomUUID().toString();
                //把用户信息存入redis
                user2redis(user, token);
                return Result.ok(token);
            }
        }
        //用户名为空或用户名错误
        //根据手机号查询
        Users users = lambdaQuery().eq(Users::getPhone, phone).one();
        if (ObjectUtil.isNotEmpty(users)) {
            //判断密码是否错误
            if (!Objects.equals(users.getPassword(), password)){
                return Result.fail("用户名，手机号或密码错误");
            }else {
                String token = UUID.randomUUID().toString();
                //把用户信息存入redis
                user2redis(users, token);
                return Result.ok(token);
            }
        }
        //用户名手机号都无法查询到用户，说明用户为注册
        return Result.fail("用户名，手机号或密码错误");
    }

    private void user2redis(Users user, String token) {
        LoginFormDTO loginFormDTO = BeanUtil.copyProperties(user, LoginFormDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(loginFormDTO, new HashMap<>(),
                //创建一个CopyOptions实例，用于配置装换选项
                CopyOptions.create()
                        //设置忽略null值的字段
                        .setIgnoreNullValue(true)
                        //设置把fieldValue转为string类型，方便存储
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        String key = USER_LOGIN_KEY + token;
        //存储到redis
        stringRedisTemplate.opsForHash().putAll(key, userMap);
        //设置过期时间为30分钟
        stringRedisTemplate.expire(key, USER_LOGIN_TTL, TimeUnit.MINUTES);
    }
}
