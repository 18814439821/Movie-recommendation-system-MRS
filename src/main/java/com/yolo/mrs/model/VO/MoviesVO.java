package com.yolo.mrs.model.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yolo
 * @since 2024-09-01
 */
@Data
@Schema(name = "MoviesVO", description = "电影基本信息VO")
public class MoviesVO implements Serializable {

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
