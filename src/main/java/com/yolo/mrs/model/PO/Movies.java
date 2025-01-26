package com.yolo.mrs.model.PO;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
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
@Schema(name = "Movies", description = "电影基本信息实体")
public class Movies implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId("movie_id")
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

    @Schema(description = "电影所属地区")
    private String district;

    @Schema(description = "电影类型")
    private String genre;
}
