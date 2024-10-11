package com.yolo.mrs.model.DTO;

import com.yolo.mrs.model.VO.ReplyVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDTO {

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

    @Schema(description = "评论者id")
    private String userId;

    @Schema(description = "评论者昵称")
    private String userName;

    @Schema(description = "评论者头像")
    private String userPhoto;

    @Schema(description = "回复列表")
    private List<ReplyVO> replyVOList;
}
