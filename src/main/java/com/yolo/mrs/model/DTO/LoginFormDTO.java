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
@Schema(name = "LoginFormDTO", description = "登录表单")
public class LoginFormDTO implements Serializable {

    @Schema(name = "username", description = "用户名")
    @NotNull(message = "用户名不能为空")
    private String username;

    @Schema(name = "password", description = "密码")
    @NotNull(message = "密码不能为空")
    private String password;

    @Schema(name = "phone", description = "手机号")
    @NotNull(message = "密码不能为空")
    private String phone;

}
