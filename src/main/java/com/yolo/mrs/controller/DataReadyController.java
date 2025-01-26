package com.yolo.mrs.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.yolo.mrs.mapper.GenreMapper;
import com.yolo.mrs.mapper.MovieMidMapper;
import com.yolo.mrs.mapper.MoviesMapper;
import com.yolo.mrs.model.PO.Genre;
import com.yolo.mrs.model.PO.MovieMid;
import com.yolo.mrs.model.PO.Movies;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IMovieCastsService;
import com.yolo.mrs.service.IMovieMidService;
import com.yolo.mrs.service.IMoviesService;
import com.yolo.mrs.utils.MovieData;
import com.yolo.mrs.utils.doubanData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.yolo.mrs.utils.RedisConstant.GENRE_TYPE;

@RestController
@RequestMapping("/DataReady")
@Tag(name = "电影数据准备接口")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class DataReadyController{

    @Resource
    IMovieMidService movieMidService;
    @Resource
    IMoviesService moviesService;
    @Resource
    MoviesMapper moviesMapper;
    @Resource
    GenreMapper genreMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private MovieMidMapper movieMidMapper;
    @Resource
    IMovieCastsService movieCastsService;

    /*爬取电影数据*/
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

    /*爬取电影封面*/
    @PostMapping("getCover")
    public Result getCover(){
        List<Movies> moviesList = moviesService.list();
        return MovieData.get(moviesList);
    }

    @PostMapping("prepare")
    public Result prepare(){
        System.out.println(" = " + stringRedisTemplate.opsForSet().members(GENRE_TYPE + "剧情"));
        return Result.ok();
    }

    /*准备类别数据*/
    @PostMapping("genreReady")
    public Result genreReady() throws InterruptedException {
        //获取中间表数据
        List<MovieMid> movieMidList = movieMidService.list();
        //遍历中间表
        for (MovieMid movieMid : movieMidList) {
            Genre genre1 = new Genre();
            String name = movieMid.getName();
            //根据中间表中的名字去电影详情表查询movie_id
            String movieId = moviesMapper.selectIdByMovieName(name).toString();
            //获取类别字符串
            String genres = movieMid.getGenre();
            String[] strings = genres.split(" ");
            //转成类别列表
            ArrayList<String> list = ListUtil.toList(strings);
            //遍历类别列表
            for (String s : list) {
                //添加到set，key为genre：type：类别名称，value为movieId
                stringRedisTemplate.opsForSet().add(GENRE_TYPE + s, movieId);
                //把类别添加到数据库的genre表中，为了后续能够查询到所有的类别信息
                Genre genre = genreMapper.selectByGenre(s);
                if (BeanUtil.isEmpty(genre)){
                    genre1.setGenreName(s);
                    //这里添加太快会导致前一个新genre还没写进去就写第二个genre，这个时候两个genre主键冲突，我们这里sleep 1s
                    genreMapper.insert(genre1);
                    Thread.sleep(1000);
                }

            }
        }
        return Result.ok();
    }

    /*movies数据存储到数据库*/
    @PostMapping("moviesDataFromFileToDatabase")
    public Result moviesDatFromFileToDatabase(){
        doubanData.readExcelToDatabase(moviesService, movieCastsService);
        return Result.ok();
    }

    /*补加movies表中的district跟genre*/
    @PostMapping("moviesDataFix")
    public Result moviesDataFix(){
        doubanData.readDatabaseToFix(moviesMapper, moviesService);
        return Result.ok();
    }

    /*movies数据存储到es*/
    @PostMapping("moviesDataToES")
    public Result moviesDataToES(){
        doubanData.readExcelToES(elasticsearchTemplate, moviesMapper);
        return Result.ok();
    }
}
