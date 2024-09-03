package com.yolo.mrs.model.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
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
@Data
@Schema(name = "Movies", description = "电影基本信息实体")
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
    private Date releaseDate;

    @Schema(description = "是否轮播")
    private Integer carousel;
}