package com.yolo.mrs.controller;

import cn.hutool.core.bean.BeanUtil;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yolo.mrs.mapper.MoviesMapper;
import com.yolo.mrs.model.DTO.ConditionForm;
import com.yolo.mrs.model.PO.MovieMid;
import com.yolo.mrs.model.PO.Movies;
import com.yolo.mrs.model.PO.MoviesDoc;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.model.VO.MoviesVO;
import com.yolo.mrs.service.IMovieMidService;
import com.yolo.mrs.service.IMoviesService;
import com.yolo.mrs.utils.MovieData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
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
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class MoviesController {

    @Resource
    private IMoviesService moviesService;
    @Resource
    private IMovieMidService movieMidService;
    @Resource
    private MoviesMapper moviesMapper;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;


    /*查询所有电影*/
    @GetMapping("movieList")
    public Result movieList(){
        return moviesService.loadIndexList();
    }

    /*查询轮播电影列表 carousel=1*/
    @GetMapping("carousel")
    public Result carousel(){
        return moviesService.carousel();
    }

    /*查询单部电影信息*/
    @PostMapping("movieDetail/id={id}")
    public Result movieDetail(@PathVariable int id){
        return moviesService.movieDetail(id);
    }

    /*条件查询电影*/
    @PostMapping("selectByCondition")
    public Result selectByCondition(@RequestBody ConditionForm conditionForm){
        return moviesService.selectByCondition(conditionForm);
    }

    @PostMapping("test")
    public Result test(@RequestBody ConditionForm conditionForm){
        String selectStr = conditionForm.getSelectStr();
        //构建查询语句
        CriteriaQuery query = new CriteriaQuery(new Criteria());
        query.addCriteria(Criteria.where("movieName").is(selectStr));
        //查询
        SearchHits<MoviesDoc> searchHits = elasticsearchTemplate.search(query, MoviesDoc.class);
        System.out.println("运行查询后的结果 = " + searchHits);
        List<SearchHit<MoviesDoc>> searchHits1 = searchHits.getSearchHits();
        System.out.println("结果中的第一层hits = " + searchHits1);
        //遍历查询结果并处理
        List<MoviesVO> moviesVOList = new ArrayList<>();
        for (SearchHit<MoviesDoc> searchHit : searchHits1) {
            //遍历第一层hits，得到里面的content，即每条查询到的movie
            MoviesDoc content = searchHit.getContent();
            moviesVOList.add(BeanUtil.copyProperties(content, MoviesVO.class));
        }
        return Result.ok(moviesVOList);
    }
}
