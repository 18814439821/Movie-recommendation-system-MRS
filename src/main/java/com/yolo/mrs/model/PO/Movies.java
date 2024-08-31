package com.yolo.mrs.model.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author yolo
 * @since 2024-09-01
 */
@Getter
@Setter
@Schema(name = "Movies", description = "")
public class Movies implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "movie_id", type = IdType.AUTO)
    private Integer movieId;

    @Schema(description = "电影名称")
    private String movieName;

    @Schema(description = "评分，10.0满分，新增可以没有评分")
    private String stars;

    @Schema(description = "电影封面")
    private String cover;

    @Schema(description = "电影名言，妙言")
    private String witticism;

    @Schema(description = "电影上映日期")
    private LocalDate releaseDate;
}
