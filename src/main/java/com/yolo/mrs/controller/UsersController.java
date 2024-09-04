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
@Tag(name = "用户接口")
public class UsersController {

    private final IUsersService usersService;

    @PostMapping("login")
    public Result login(@RequestBody @Validated LoginForm loginForm){
        return usersService.login(loginForm);
    }

    @GetMapping("getCode")
    public Result getCode(){
        return usersService.getCode();
    }
}
