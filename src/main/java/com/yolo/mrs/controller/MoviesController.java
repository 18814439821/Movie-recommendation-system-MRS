package com.yolo.mrs.controller;

import com.yolo.mrs.model.PO.MovieMid;
import com.yolo.mrs.model.PO.Movies;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IMovieMidService;
import com.yolo.mrs.service.IMoviesService;
import com.yolo.mrs.utils.MovieData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;



/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yolo
 * @since 2024-09-01
 */
@RestController
@RequestMapping("/movies")
@Tag(name = "电影基本信息接口")
public class MoviesController {

    @Resource
    private IMoviesService moviesService;
    @Resource
    private IMovieMidService movieMidService;

    @PostMapping("movieData")
    public Result movieData(){
        List<Movies> moviesList = new ArrayList<>();
        List<MovieMid> movieMidList = new ArrayList<>();
        MovieData.MovieDataReady(moviesList, movieMidList);
        for (Movies movie : moviesList) {
            moviesService.save(movie);
        }
        for (MovieMid movieMid : movieMidList) {
            movieMidService.save(movieMid);
        }
        return Result.ok("数据准备完成");
    }

    @PostMapping("getCover")
    public Result getCover(){
        List<Movies> moviesList = moviesService.list();
        return MovieData.get(moviesList);
    }

    @GetMapping("movieList")
    public Result movieList(){
        return moviesService.loadIndexList();
    }

    @GetMapping("carousel")
    public Result carousel(){
        return moviesService.carousel();
    }

}
