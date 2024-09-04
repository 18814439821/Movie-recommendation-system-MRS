package com.yolo.mrs.utils;

import com.yolo.mrs.model.DTO.UsersDTO;

public class UserHolder {

    private static final ThreadLocal<UsersDTO> tl = new ThreadLocal<>();

    public static void setUser(UsersDTO usersDTO){
        tl = usersDTO;
    }

    public static UsersDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }

}
