package com.yolo.mrs.model.PO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@Schema(name = "Blog", description = "")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "题目")
    private String title;

    @Schema(description = "绑定电影id")
    private Integer movieId;

    @Schema(description = "绑定文章内容id")
    private String blogContentId;

    @Schema(description = "作者id")
    private Integer authorId;

    @Schema(description = "编写时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @Schema(description = "0-未审核，1-审核通过，2-审核不通过")
    private Integer status;
}
