package com.yolo.mrs.service.impl;

import com.yolo.mrs.model.PO.Genre;
import com.yolo.mrs.mapper.GenreMapper;
import com.yolo.mrs.service.IGenreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yolo
 * @since 2024-09-10
 */
@Service
public class GenreServiceImpl extends ServiceImpl<GenreMapper, Genre> implements IGenreService {

}
