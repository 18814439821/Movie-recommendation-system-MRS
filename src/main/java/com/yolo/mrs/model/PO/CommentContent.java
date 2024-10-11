package com.yolo.mrs.model.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("comment_content")
@Schema(name = "CommentContent", description = "")
public class CommentContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "评论内容id")
    private String contentId;

    @Schema(description = "评论内容")
    private String content;
}
