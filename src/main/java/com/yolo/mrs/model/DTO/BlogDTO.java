package com.yolo.mrs.model.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
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
@Data
@Schema(name = "Blog", description = "blogDto")
public class BlogDTO implements Serializable {

    @Schema(description = "题目")
    private String title;

    @Schema(description = "绑定电影id")
    private Integer movieId;

    @Schema(description = "作者名")
    private String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    @Schema(description = "编写时间")
    private LocalDateTime createTime;

    @Schema(description = "文章内容")
    private String contents;

}
