package com.yolo.mrs.model.DTO;

import lombok.Data;

@Data
public class UserInfoDTO {
    private String username;

    private String email;

    private String phone;

    private String photo;

    private Integer userId;

    private String token;
}
