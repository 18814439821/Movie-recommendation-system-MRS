package com.yolo.mrs.controller;

import com.yolo.mrs.model.DTO.BlogDTO;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IBlogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yolo
 * @since 2025-01-05
 */
@RestController
@RequestMapping("/blog")
@CrossOrigin("http://localhost:8080")
public class BlogController {

    @Resource
    IBlogService blogService;

    @PostMapping("/saveBlog")
    public Result saveBlog(@RequestBody BlogDTO blogDTO){
        return blogService.saveBlog(blogDTO);
    }
}
