package com.yolo.mrs.mapper;

import com.yolo.mrs.model.PO.Carousel;
import com.yolo.mrs.model.PO.Movies;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yolo
 * @since 2024-09-01
 */
@Mapper
@Tag(name = "电影信息接口")
public interface MoviesMapper extends BaseMapper<Movies> {
    @Select("SELECT movie_id FROM movies WHERE carousel = 1")
    List<Carousel> selectForCarousel();
}