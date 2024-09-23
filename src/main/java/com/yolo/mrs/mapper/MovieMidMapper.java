package com.yolo.mrs.mapper;

import com.yolo.mrs.model.PO.MovieMid;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yolo
 * @since 2024-09-01
 */
@Mapper
public interface MovieMidMapper extends BaseMapper<MovieMid> {

    @Select("select * from movie_mid where name = #{movieName}")
    MovieMid selectByMovieName(String movieName);
}
