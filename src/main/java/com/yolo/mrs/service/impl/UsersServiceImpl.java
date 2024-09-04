package com.yolo.mrs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import com.yolo.mrs.model.DTO.LoginForm;
import com.yolo.mrs.model.DTO.UsersDTO;
import com.yolo.mrs.model.PO.Users;
import com.yolo.mrs.mapper.UsersMapper;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yolo.mrs.utils.CaptchaGenerator;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.yolo.mrs.utils.Constant.*;
import static com.yolo.mrs.utils.RedisConstant.*;

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
    public Result login(LoginForm loginForm){
        String loginUser = loginForm.getLoginUser();
        String password = loginForm.getPassword();
        String username = "";
        String phone = "";
        //判断是手机号还是用户名
        int check = checkFirstChar(loginUser);
        if (check == 1){
            username = loginUser;
        }else if (check == 0){
            phone = loginUser;
        }
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
        //用户名手机号都无法查询到用户，说明用户未注册，我们帮用户自动注册
        Users signUser = new Users();
        if (check == 0){
            String signUsername = java.util.UUID.randomUUID().toString();
            signUser.setPhone(phone);
            //因为生成的UUID是带有-分割的，我们不需要带-
            signUser.setUsername(signUsername.replaceAll("-",""));
            signUser.setPassword(password);
            boolean flag = save(signUser);
            if (flag){
                String token = UUID.randomUUID().toString();
                user2redis(signUser, token);
                return Result.ok(token);
            }else {
                Result.fail("注册失败");
            }
        }
        return Result.fail("用户名，手机号或密码错误");
    }


    @Override
    public Result getCode() {
        CaptchaGenerator captchaGenerator = new CaptchaGenerator();
        String captcha = captchaGenerator.getCaptcha();
        if (captcha == null) {
            // 处理错误情况，例如返回错误响应
            return Result.fail("验证码生成失败");
        }
        //获取验证码
        String[] strings = captcha.split("@");
        Random random = new Random();
        //生成随机id，一个id对应一个验证码
        int id = random.nextInt(CODE_MAX);
        String key = USER_LOGIN_CODE + ":" + id ;
        //写入redis
        stringRedisTemplate.opsForValue().set(key, strings[1]);
        stringRedisTemplate.expire(key, USER_LOGIN_CODE_TTL, TimeUnit.MINUTES);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("captchaId", id);
        responseData.put("captchaImage", strings[0]);
        return Result.ok(responseData);
    }

    private void user2redis(Users user, String token) {
        UsersDTO usersDTO = BeanUtil.copyProperties(user, UsersDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(usersDTO, new HashMap<>(),
                //创建一个CopyOptions实例，用于配置装换选项
                CopyOptions.create()
                        //设置忽略null值的字段
                        .setIgnoreNullValue(true)
                        //设置把fieldValue转为string类型，方便存储
                        .setFieldValueEditor((fieldName, fieldValue) -> {
                            //因为setIgnoreNullValues是在构建Map时生效，但是这里在遍历时任然会有NPE，所以我们应该在处理时额外对null进行判断。
                            return fieldValue != null ? fieldValue.toString() : "";
                        }));
        String key = USER_LOGIN_KEY + token;
        //存储到redis
        stringRedisTemplate.opsForHash().putAll(key, userMap);
        //设置过期时间为30分钟
        stringRedisTemplate.expire(key, USER_LOGIN_TTL, TimeUnit.MINUTES);
    }

    private static int checkFirstChar(String checkString) {
        char firstChar = checkString.charAt(0);
        if (Character.isDigit(firstChar)){
            return CHAR_FIRST_INT;
        } else if (Character.isLetter(firstChar)) {
            return CHAR_FIRST_STR;
        } else {
            return CHAR_FIRST_SP;
        }
    }
}
