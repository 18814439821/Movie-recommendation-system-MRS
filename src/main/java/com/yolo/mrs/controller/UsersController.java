package com.yolo.mrs.controller;

import com.yolo.mrs.model.DTO.LoginForm;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
}
