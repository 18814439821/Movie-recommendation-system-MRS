package com.yolo.mrs.service;

import com.yolo.mrs.model.DTO.BlogDTO;
import com.yolo.mrs.model.PO.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yolo.mrs.model.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yolo
 * @since 2025-01-05
 */
public interface IBlogService extends IService<Blog> {

    Result saveBlog(BlogDTO blogDTO);
}
