package com.yolo.mrs.mapper;

import com.yolo.mrs.model.PO.CommentContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yolo
 * @since 2024-09-23
 */
public interface CommentContentMapper extends BaseMapper<CommentContent> {
    @Select("select * from comment_content where content_id = #{id}")
    CommentContent getByContentId(String id);
}
