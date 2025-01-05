package com.yolo.mrs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yolo.mrs.model.DTO.LoginForm;
import com.yolo.mrs.model.DTO.PwdFormDTO;
import com.yolo.mrs.model.DTO.UserInfoDTO;
import com.yolo.mrs.model.DTO.UsersDTO;
import com.yolo.mrs.model.PO.Users;
import com.yolo.mrs.mapper.UsersMapper;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.model.VO.UsersVO;
import com.yolo.mrs.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yolo.mrs.utils.CaptchaGenerator;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
    @Autowired
    private UsersMapper usersMapper;

    /*登录接口*/
    @Override
    public Result login(LoginForm loginForm){
        String loginUser = loginForm.getLoginUser();
        String password = loginForm.getPassword();
        String username = "";
        String phone = "";
        //判断是手机号还是用户名
        int check = checkFirstChar(loginUser);
        if (check == CHAR_FIRST_STR){
            username = loginUser;
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
                    UsersVO usersVO = BeanUtil.copyProperties(user, UsersVO.class);
                    usersVO.setToken(token);
                    return Result.ok(usersVO);
                }
            }
        }else if (check == CHAR_FIRST_INT){
            phone = loginUser;
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
                    UsersVO usersVO = BeanUtil.copyProperties(users, UsersVO.class);
                    usersVO.setToken(token);
                    return Result.ok(usersVO);
                }
            }
        }
        //用户名手机号都无法查询到用户，说明用户未注册，我们帮用户自动注册
        Users signUser = new Users();
        //先设置密码，因为不管是手机号注册还是用户名注册都要密码
        signUser.setPassword(password);
        //设置默认头像
        signUser.setPhoto("default.png");
        //数字开头，是手机号注册
        if (check == CHAR_FIRST_INT){
            String signUsername = java.util.UUID.randomUUID().toString();
            signUser.setPhone(phone);
            //因为生成的UUID是带有-分割的，我们不需要带-
            signUser.setUsername(signUsername.replaceAll("-",""));
            boolean flag = save(signUser);
            if (flag){
                String token = UUID.randomUUID().toString();
                user2redis(signUser, token);
                return Result.ok(token);
            }else {
                Result.fail("注册失败");
            }
        } else if (check == CHAR_FIRST_STR) {
            //字符开头，是用户名也就是账号注册
            signUser.setUsername(loginUser);
            boolean flag1 = save(signUser);
            if (flag1){
                String token = UUID.randomUUID().toString();
                user2redis(signUser, token);
                return Result.ok(token);
            }else {
                Result.fail("注册失败");
            }
        }
        return Result.fail("用户名，手机号或密码错误");
    }

    /*验证码接口*/
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

    /*根据token校验用户登录状态-登录过期机制*/
    @Override
    public int check(String token) {
        String token1 = JSONUtil.toBean(token, Map.class).get("token").toString();
        Boolean flag = stringRedisTemplate.hasKey(USER_LOGIN_KEY + token1);
        if (Boolean.TRUE.equals(flag)){
            return 1;
        }
        return 0;
    }

    /*用户登出*/
    @Override
    public int logout(String token) {
        String token1 = JSONUtil.toBean(token, Map.class).get("token").toString();
        Boolean delete = stringRedisTemplate.delete(USER_LOGIN_KEY + token1);
        System.out.println("delete = " + delete);
        if (Boolean.TRUE.equals(delete)){
            return 1;
        }
        return 0;
    }

    /*用户名重复校验*/
    @Override
    public Result userCheck(Users user) {
        String username = user.getUsername();
        System.out.println("user.getUsername() = " + username);
        //1.在redis中查询是否有该用户名
        String s = stringRedisTemplate.opsForValue().get(USER_CHECK_USERNAME + username);
        if (s != null){
            //1.1如果存在该用户名则直接返回true
            return Result.ok();
        }else{
            //1.2如果不存在就去数据库查询
            Users users = usersMapper.selectByUserName(username);
            if (users != null){
                //1.2.1如果存在该用户名则直接返回true，并且把用户名存到redis中
                stringRedisTemplate.opsForValue().set(USER_CHECK_USERNAME + username, username);
                stringRedisTemplate.expire(USER_CHECK_USERNAME + username, USER_CHECK_USERNAME_TTL, TimeUnit.MINUTES);
                return Result.ok();
            }
        }
        return Result.fail("");
    }

    /*修改用户信息*/
    @Override
    public Result updateUserInfo(UserInfoDTO user) {
        //1.判断user中是否有token
        String token = user.getToken();
        if (StrUtil.isBlank(token) && StrUtil.isEmpty(token)){
            return Result.fail("未携带token");
        }
        //2.根据user中的token在redis中查询userId
        String userId = getUserIdFromRedisByToken(token);
        //3.然后根据userId修改用户信息
        Users saveUser = BeanUtil.copyProperties(user, Users.class);
        saveUser.setUserId(Integer.valueOf(userId));
        boolean b = updateById(saveUser);
        if (b){
            return Result.ok();
        }
        return Result.fail("保存失败");
    }

    /*根据token获取用户信息*/
    private String getUserIdFromRedisByToken(String token) {
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + token);
        return (String) entries.get("userId");
    }

    /*获取用户头像（后续已弃用）*/
    @Override
    public Result getUserPhoto(String token) {
        //1.根据token获取userId
        //1.1判断是否存在token
        if (StrUtil.isBlank(token) && StrUtil.isEmpty(token)){
            try {
                //1.1.1读取文件内容为字节数组
                byte[] fileBytes = Files.readAllBytes(Paths.get(USER_PHOTO_UNLOGIN));
                //1.1.2将字节数组编码为Base64字符串
                String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(fileBytes);
                //1.1.3返回包含Base64字符串的Result对象
                return Result.ok(base64Image);
            } catch (IOException e) {
                //1.1.4处理IO异常，例如记录日志或返回错误响应
                e.printStackTrace();
                return Result.fail("头像访问异常");
            }
        }
        //1.2存在token
        String userId = getUserIdFromRedisByToken(token);
        //2.构建文件路径
        Path path = Paths.get(USER_PHOTO_PATH + userId + ".png");
        //3.检查文件是否存在
        if (!Files.exists(path)) {
            //4.文件不存在，直接访问默认头像
            try {
                //4.1读取文件内容为字节数组
                byte[] fileBytes = Files.readAllBytes(Paths.get(USER_PHOTO_DEFAULT));
                //4.2将字节数组编码为Base64字符串
                String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(fileBytes);
                //4.3返回包含Base64字符串的Result对象
                return Result.ok(base64Image);
            } catch (IOException e) {
                //4.4处理IO异常，例如记录日志或返回错误响应
                e.printStackTrace();
                return Result.fail("头像访问异常");
            }
        }
        //5.文件存在
        try {
            //5.1读取文件内容为字节数组
            byte[] fileBytes = Files.readAllBytes(path);
            //5.2将字节数组编码为Base64字符串
            String base64Image = Base64.getEncoder().encodeToString(fileBytes);
            //5.3返回包含Base64字符串的Result对象
            return Result.ok("data:image/png;base64," + base64Image);
        } catch (IOException e) {
            //5.4处理IO异常，例如记录日志或返回错误响应
            e.printStackTrace();
            return Result.fail("头像访问异常");
        }
    }

    /*修改密码*/
    @Override
    public Result updatePwd(PwdFormDTO pwdForm) {
        String token = pwdForm.getToken();
        String userId = ""
;        //1.判断是否含有token
        if (StrUtil.isBlank(token) && StrUtil.isEmpty(token)){
            //没有token则返回用户未登录，或者用户登录已过期
            return Result.fail("用户登录已过期或未登录");
        }
        //2.带有token就用token去redis查userId
        userId = getUserIdFromRedisByToken(token);
        //3.根据userId查询用户密码
        String oldPwd = usersMapper.selectPwdById(userId);
        //4.判断旧密码是否一致
        if (!Objects.equals(oldPwd, pwdForm.getOldPassword())){
            //4.1不一致，直接返回原密码错误
            return Result.fail("原密码错误");
        }
        //4.2密码一致
        //5.修改密码
        usersMapper.updatePwdByUserId(pwdForm.getNewPassword(),userId);
        //6.删除token
        Boolean delete = stringRedisTemplate.delete("USER_LOGIN_KEY" + token);
        if (Boolean.TRUE.equals(delete)){
            return Result.ok();
        }
        return Result.fail("修改密码失败，请联系管理员");
    }

    /*存储用户到redis-token*/
    private void user2redis(Users user, String token) {
        UsersDTO usersDTO = BeanUtil.copyProperties(user, UsersDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(usersDTO, new HashMap<>(),
                //创建一个CopyOptions实例，用于配置转换选项
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

    /*检查登录账号格式*/
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
