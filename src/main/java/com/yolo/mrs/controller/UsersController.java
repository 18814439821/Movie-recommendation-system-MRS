package com.yolo.mrs.controller;

import com.yolo.mrs.model.DTO.LoginForm;
import com.yolo.mrs.model.DTO.PwdFormDTO;
import com.yolo.mrs.model.DTO.UserInfoDTO;
import com.yolo.mrs.model.DTO.UsersDTO;
import com.yolo.mrs.model.PO.Users;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yolo
 * @since 2024-08-29
 */

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@Tag(name = "用户接口")
public class UsersController {

    private final IUsersService usersService;

    /*登录*/
    @PostMapping("login")
    public Result login(@RequestBody @Validated LoginForm loginForm){
        return usersService.login(loginForm);
    }

    /*获取验证码*/
    @GetMapping("getCode")
    public Result getCode(){
        return usersService.getCode();
    }

    /*token检查*/
    @PostMapping("check")
    public int check(@RequestBody String token){
        return usersService.check(token);
    }

    /*登出*/
    @PostMapping("logout")
    public int logout(@RequestBody String token){
        return usersService.logout(token);
    }

    /*用户名校验*/
    @PostMapping("userCheck")
    public Result userCheck(@RequestBody Users user){
        return usersService.userCheck(user);
    }

    /*用户信息修改*/
    @PostMapping("updateUserInfo")
    public Result updateUserInfo(@RequestBody UserInfoDTO user){
        return usersService.updateUserInfo(user);
    }

    /*获取用户头像*/
    @PostMapping("getUserPhoto/token={token}")
    public Result getUserPhoto(@PathVariable String token){
        return usersService.getUserPhoto(token);
    }

    /*修改密码*/
    @PostMapping("updatePwd")
    public Result updatePwd(@RequestBody PwdFormDTO pwdForm){
        return usersService.updatePwd(pwdForm);
    }


}
