package com.yolo.mrs.service;

import com.yolo.mrs.model.PO.Movies;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yolo.mrs.model.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yolo
 * @since 2024-09-01
 */
public interface IMoviesService extends IService<Movies> {

    Result loadIndexList();

    Result carousel();
}
