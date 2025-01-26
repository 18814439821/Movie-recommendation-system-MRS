package com.yolo.mrs.model.PO;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
@Data
@Document(indexName = "moviesdata")
public class MoviesDoc {
    @Field(type = FieldType.Text)
    @ExcelProperty("电影id")
    private Integer movieId;

    @Field(type = FieldType.Text)
    @ExcelProperty("电影名称")
    private String movieName;

    @Field(type = FieldType.Text, analyzer = "standard")
    @ExcelProperty("电影评分")
    private String stars;

    @Field(type = FieldType.Date)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // 确保返回给前端的日期格式正确
    @ExcelProperty("上映日期")
    private Date releaseDate;

    @Field(type = FieldType.Text, analyzer = "whitespace") // 使用空格分词器
    @ExcelProperty("电影所属类别")
    private String genre;

    @Field(type = FieldType.Text, analyzer = "whitespace")
    @ExcelProperty("地区")
    private String district;

    @Field(type = FieldType.Text, analyzer = "whitespace")
    @ExcelProperty("演员列表")
    private String casts;
}
