package com.yolo.mrs.service;

import com.yolo.mrs.model.DTO.CommentForm;
import com.yolo.mrs.model.PO.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yolo.mrs.model.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yolo
 * @since 2024-09-23
 */
public interface ICommentService extends IService<Comment> {

    Result addComment(CommentForm commentForm);

    Result getBasicComment(String movieId);
}
