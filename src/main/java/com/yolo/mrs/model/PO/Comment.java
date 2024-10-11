package com.yolo.mrs.model.PO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author yolo
 * @since 2024-09-25
 */
@Getter
@Setter
@Schema(name = "Comment", description = "")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "评论信息id")
    private String commentId;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "电影id")
    private String movieId;

    @Schema(description = "评论内容id")
    private String contentId;

    @Schema(description = "评论时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "状态")
    private String status;

    private LocalDateTime updateTime;
}
