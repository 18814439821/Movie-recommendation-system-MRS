package com.yolo.mrs.utils;

import com.yolo.mrs.model.DTO.UsersDTO;
import com.yolo.mrs.model.VO.UsersVO;

public class UserHolder {

    private static final ThreadLocal<UsersVO> tl = new ThreadLocal<>();

    public static void setUser(UsersVO usersVO){
        tl.set(usersVO);
    }

    public static UsersVO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }

}
