package com.yolo.mrs.controller;

import com.yolo.mrs.model.DTO.CommentForm;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.ICommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yolo
 * @since 2024-09-23
 */
@RestController
@RequestMapping("/comment")
@Tag(name = "评论管理接口")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class CommentController {

    @Resource
    ICommentService commentService;

    @PostMapping("addComment")
    public Result addComment(@RequestBody CommentForm commentForm){
        return commentService.addComment(commentForm);
    }

    @PostMapping("getBasicComment/id={id}")
    public Result getBasicComment(@PathVariable String id){
        return commentService.getBasicComment(id);
    }

}
