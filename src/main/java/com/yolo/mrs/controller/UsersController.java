package com.yolo.mrs.controller;

import com.yolo.mrs.model.DTO.LoginFormDTO;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
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

    @Operation(summary = "登录", description = "传入登录账号/手机号，密码")
    @PostMapping("login")
    public Result login(@RequestBody @Validated LoginFormDTO loginFormDTO){
        return usersService.login(loginFormDTO);
    }
}
