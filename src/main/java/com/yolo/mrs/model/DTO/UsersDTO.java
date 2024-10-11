package com.yolo.mrs.model.DTO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yolo
 * @since 2024-08-29
 */
@Data
@Schema(name = "User", description = "$!{table.comment}")
@EqualsAndHashCode
@TableName("users")
public class UsersDTO implements Serializable {

    private String username;

    private String password;

    private String email;

    private String phone;

    private String photo;

    private Integer userId;
}
