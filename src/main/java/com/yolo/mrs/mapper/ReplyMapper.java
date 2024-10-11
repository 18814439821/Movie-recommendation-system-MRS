package com.yolo.mrs.mapper;

import com.yolo.mrs.model.PO.Reply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yolo.mrs.model.VO.CommentVO;
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
public interface ReplyMapper extends BaseMapper<Reply> {
    @Select("select * from reply where comment_id = #{commmentId}")
    List<Reply> selectByCommentId(String commentId);
}
