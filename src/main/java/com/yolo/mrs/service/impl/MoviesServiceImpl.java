package com.yolo.mrs.service.impl;

import com.yolo.mrs.model.PO.Movies;
import com.yolo.mrs.mapper.MoviesMapper;
import com.yolo.mrs.service.IMoviesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yolo
 * @since 2024-09-01
 */
@Service
public class MoviesServiceImpl extends ServiceImpl<MoviesMapper, Movies> implements IMoviesService {

}
