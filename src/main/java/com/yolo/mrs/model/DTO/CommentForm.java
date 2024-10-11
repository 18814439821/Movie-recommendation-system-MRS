package com.yolo.mrs.model.DTO;


import lombok.Data;

@Data
public class CommentForm {
    /*评论的电影id*/
    private String movieId;
    /*评论的内容*/
    private String content;
    /*评论者id*/
    private String aUserName;
    /*被评论者id*/
    private String bUserName;
    /*被评论id*/
    private String commentId;
    /*评论模式 1是添加根评论，2是回复根评论，3是回复回复*/
    private String flag;
}
