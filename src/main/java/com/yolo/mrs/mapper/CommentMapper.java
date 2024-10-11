package com.yolo.mrs.mapper;

import com.yolo.mrs.model.PO.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yolo
 * @since 2024-09-23
 */
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("select * from comment where movie_id = #{movieId}")
    List<Comment> getListByMovieId(String movieId);
}
