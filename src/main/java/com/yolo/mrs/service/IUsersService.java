package com.yolo.mrs.service;

import com.yolo.mrs.model.DTO.LoginForm;
import com.yolo.mrs.model.PO.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yolo.mrs.model.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yolo
 * @since 2024-08-29
 */
public interface IUsersService extends IService<Users> {

    Result login(LoginForm loginForm);

    Result getCode();
}
