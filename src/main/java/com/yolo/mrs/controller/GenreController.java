package com.yolo.mrs.controller;

import com.yolo.mrs.model.PO.Genre;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IGenreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yolo
 * @since 2024-09-10
 */
@RestController
@RequestMapping("/genre")
@Tag(name = "类别接口")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class GenreController {

    @Resource
    IGenreService genreService;

    @PostMapping("genreList")
    public Result genreList(){
        List<Genre> genreList = genreService.list();
        ArrayList<String> list = new ArrayList<>();
        for (Genre genre : genreList) {
            list.add(genre.getGenreName());
        }
        return Result.ok(list);
    }
}
