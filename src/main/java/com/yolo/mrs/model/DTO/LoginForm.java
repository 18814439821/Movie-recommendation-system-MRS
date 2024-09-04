package com.yolo.mrs.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yolo
 * @since 2024-08-29
 */
@Data
@Schema(name = "LoginForm", description = "登录表单")
public class LoginForm implements Serializable {

    @Schema(name = "loginUser", description = "登录用户")
    @NotNull(message = "用户名/手机号不能为空")
    private String loginUser;

    @Schema(name = "password", description = "密码")
    @NotNull(message = "密码不能为空")
    private String password;

    @Schema(name = "codeId", description = "验证码id")
    @NotNull(message = "验证码id不能为空")
    private String codeId;

    @Schema(name = "code", description = "验证码")
    @NotNull(message = "验证码不能为空")
    private String code;
}
