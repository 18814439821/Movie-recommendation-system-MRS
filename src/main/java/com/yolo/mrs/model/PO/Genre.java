package com.yolo.mrs.model.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2024-09-10
 */
@Getter
@Setter
@Schema(name = "Genre", description = "")
public class Genre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "genre_id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "类别名称")
    private String genreName;
}
