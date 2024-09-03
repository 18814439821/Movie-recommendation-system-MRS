package com.yolo.mrs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yolo.mrs.model.PO.Carousel;
import com.yolo.mrs.model.PO.Movies;
import com.yolo.mrs.mapper.MoviesMapper;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.model.VO.MoviesVO;
import com.yolo.mrs.service.IMoviesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static com.yolo.mrs.utils.Constant.INDEX_LIST_COUNT;
import static com.yolo.mrs.utils.Constant.INDEX_LIST_NUM;

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

    @Resource
    MoviesMapper moviesMapper;

    @Override
    public Result loadIndexList() {
        IPage<Movies> page = new Page<>(INDEX_LIST_NUM, INDEX_LIST_COUNT);
        IPage<Movies> list = query().orderByDesc("stars").page(page);
        List<Movies> moviesList = list.getRecords();
        List<MoviesVO> moviesVOList = moviesList.stream()
                .map(movies -> BeanUtil.copyProperties(movies, MoviesVO.class))
                .toList();
        return Result.ok(moviesVOList,list.getTotal());
    }

    @Override
    public Result carousel() {
        List<Carousel> carousels = moviesMapper.selectForCarousel();
        return Result.ok(carousels);
    }

}
