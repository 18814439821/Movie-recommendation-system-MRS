package com.yolo.mrs.model.DTO;


import lombok.Data;

@Data
public class PwdFormDTO {
    private String newPassword;
    private String oldPassword;
    private String token;
}
