package com.yolo.mrs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yolo.mrs.mapper.MoviesMapper;
import com.yolo.mrs.model.DTO.ConditionForm;
import com.yolo.mrs.model.PO.Carousel;
import com.yolo.mrs.model.PO.Movies;
import com.yolo.mrs.model.PO.MoviesDoc;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.model.VO.MoviesVO;
import com.yolo.mrs.service.IMoviesService;
import jakarta.annotation.Resource;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Resource
    ElasticsearchTemplate elasticsearchTemplate;


    /*主页展示列表*/
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

    /*主页轮播列表*/
    @Override
    public Result carousel() {
        //首页轮播电影
        List<Carousel> carousels = moviesMapper.selectForCarousel();
        return Result.ok(carousels);
    }

    /*电影详情*/
    @Override
    public Result movieDetail(int id) {
        Criteria criteria = new Criteria("movieId").is(id);
        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHit<MoviesDoc> moviesDocSearchHit = elasticsearchTemplate.searchOne(query, MoviesDoc.class);
        if (moviesDocSearchHit != null){
            MoviesVO moviesVO = BeanUtil.copyProperties(moviesDocSearchHit.getContent(), MoviesVO.class);
            return Result.ok(moviesVO);
        }
        return Result.fail("查询失败，请刷新页面或联系管理员！");
    }

    /*根据条件搜索*/
    @Override
    public Result selectByCondition(ConditionForm conditionForm) {
        System.out.println("conditionForm = " + conditionForm);
        //准备es查询语句
        Criteria criteria = new Criteria();
        //处理前端传参
        //判断是否有类别查询条件
        if (!conditionForm.getGenre().isEmpty()){
            criteria.and(new Criteria("genre").matches(conditionForm.getGenre()));
        }
        //判断是否有查询电影名称，如果有则添加查询条件
        if (!StrUtil.isEmpty(conditionForm.getSelectStr())){
            System.out.println("selectStr = " + conditionForm.getSelectStr());
            criteria.and(new Criteria("movieName").matches(conditionForm.getSelectStr()));
        }
        //判断是否有地区筛选条件
        //判断是否有上映时间筛选条件
        if(conditionForm.getReleaseDate() != null){
            //获取的日期为一年的第一天
            LocalDate startDate = conditionForm.getReleaseDate();
            //加一年之后设置为范围的最后一天
            LocalDate endDate = startDate.plusYears(1);
            criteria.and(new Criteria("releaseDate").greaterThanEqual(startDate).lessThan(endDate));
        }
        //判断是否有排序条件
        //判断是否有排序方式
        System.out.println("criteria = " + criteria);
        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<MoviesDoc> searchHits = elasticsearchTemplate.search(query, MoviesDoc.class);
        System.out.println("searchHits = " + searchHits);
        ArrayList<MoviesVO> movieList = new ArrayList<>();
        for (SearchHit<MoviesDoc> searchHit : searchHits) {
            MoviesVO moviesVO = BeanUtil.copyProperties(searchHit.getContent(), MoviesVO.class);
            movieList.add(moviesVO);
        }

        return Result.ok(movieList, (long) movieList.size());
    }

    /*查询电影名称列表及id*/
    @Override
    public Result movieNameAndIdList() {
        List<Movies> moviesList = moviesMapper.selectMovieNameAndIdList();
        List<Map<String, String>> collect = moviesList.parallelStream().map(movies -> {
            Integer movieId = movies.getMovieId();
            String movieName = movies.getMovieName();
            Map<String, String> map = new HashMap<>();
            map.put("movieId", movieId.toString());
            map.put("movieName", movieName);
            return map;
        }).collect(Collectors.toList());
        return Result.ok(collect, (long) moviesList.size());
    }

}
