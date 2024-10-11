package com.yolo.mrs.mapper;

import com.yolo.mrs.model.PO.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yolo.mrs.model.VO.UsersVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yolo
 * @since 2024-08-29
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    @Select("select * from users where username = #{userName}")
    Users selectByUserName(String userName);

    @Update("update users set photo = #{s} where user_id = #{userId}")
    void updateUserPhoto(String s, Integer userId);

    @Select("select password from users where user_id = #{userId}")
    String selectPwdById(String userId);

    @Update("update users set password = #{newPassword} where user_id = #{userId}")
    void updatePdwByUserId(String newPassword, String userId);
}
