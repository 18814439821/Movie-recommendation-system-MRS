package com.yolo.mrs.model.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ConditionForm {
    /*搜索字段，包括电影名称，演员，导演等*/
    private String selectStr;
    /*类型*/
    private List<String> genre;
    /*上映年份*/
    private LocalDate releaseDate;
    /*地区*/
    private String region;
    /*排序条件*/
    private String orderBy;
    /*排序方式*/
    private String sortord;
}
