package com.yolo.mrs.model.PO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yolo
 * @since 2025-01-05
 */
@Getter
@Setter
@TableName("blog_content")
@Schema(name = "BlogContent", description = "")
public class BlogContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId("blog_content_id")
    private String blogContentId;

    @Schema(description = "文章内容")
    private String blogContent;
}
