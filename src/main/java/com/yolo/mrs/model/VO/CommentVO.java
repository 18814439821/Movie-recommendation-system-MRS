package com.yolo.mrs.model.VO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yolo.mrs.model.PO.CommentContent;
import com.yolo.mrs.model.PO.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentVO {

    @Schema(description = "评论信息id")
    private String commentId;

    @Schema(description = "电影id")
    private String movieId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "评论时间")
    private LocalDateTime createTime;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "评论内容id")
    private String contentId;

    @Schema(description = "评论者昵称")
    private String userName;

    @Schema(description = "评论者头像")
    private String userPhoto;

    @Schema(description = "回复列表")
    private List<ReplyVO> replyVOList;
}
