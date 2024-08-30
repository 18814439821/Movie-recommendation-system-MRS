package com.yolo.mrs.model.VO;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class UsersVO implements Serializable {

    private String username;

    private String email;

    private String phone;

    private String photo;

    private String userLevel;

    private String token;
}
