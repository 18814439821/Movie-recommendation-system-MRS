package com.yolo.mrs.model.PO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
@Data
@Document(indexName = "movies")
public class MoviesDoc {
    @Id
    private Integer movieId;

    @Field(type = FieldType.Text)
    private String movieName;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String stars;

    @Field(type = FieldType.Text)
    private String cover;

    @Field(type = FieldType.Text)
    private String witticism;

    @Field(type = FieldType.Date)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // 确保日期格式正确
    private Date releaseDate;

    @Field(type = FieldType.Text, analyzer = "whitespace") // 使用空格分词器
    private String genre;
}
