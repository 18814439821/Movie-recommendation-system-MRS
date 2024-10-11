package com.yolo.mrs.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyDTO {

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "评论时间")
    private LocalDateTime createTime;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "评论内容id")
    private String contentId;

    @Schema(description = "回复者昵称")
    private String aUserName;

    @Schema(description = "回复者id")
    private String aUserId;

    @Schema(description = "回复者头像")
    private String aUserPhoto;

    @Schema(description = "被回复者id")
    private String bUserId;

    @Schema(description = "被回复者昵称")
    private String bUserName;

}
