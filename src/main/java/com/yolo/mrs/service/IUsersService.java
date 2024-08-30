package com.yolo.mrs.service;

import com.yolo.mrs.model.DTO.LoginFormDTO;
import com.yolo.mrs.model.PO.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yolo.mrs.model.Result;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.BadRequestException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yolo
 * @since 2024-08-29
 */
public interface IUsersService extends IService<Users> {

    Result login(LoginFormDTO loginFormDTO);
}
