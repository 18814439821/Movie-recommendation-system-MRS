package com.yolo.mrs.service.impl;

import com.yolo.mrs.model.PO.Comment;
import com.yolo.mrs.mapper.CommentMapper;
import com.yolo.mrs.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yolo
 * @since 2024-09-23
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
