package com.yolo.mrs.mapper;

import com.yolo.mrs.model.PO.Genre;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yolo
 * @since 2024-09-10
 */
public interface GenreMapper extends BaseMapper<Genre> {

    @Select("select * from  genre  where genre_name = #{s}")
    Genre selectByGenre(String s);
}
