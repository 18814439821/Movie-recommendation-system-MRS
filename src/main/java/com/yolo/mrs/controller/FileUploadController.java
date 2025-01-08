package com.yolo.mrs.controller;

import cn.hutool.core.util.StrUtil;
import com.yolo.mrs.mapper.UsersMapper;
import com.yolo.mrs.model.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static com.yolo.mrs.utils.Constant.*;
import static com.yolo.mrs.utils.RedisConstant.USER_LOGIN_KEY;


@RestController
@RequestMapping("/fileUpload")
@Tag(name = "文件上传接口")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class FileUploadController {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    UsersMapper usersMapper;
    @Value("${server.port}")
    String port;

    @PostMapping("photoUpLoad")
    public Result photoUpload(@RequestParam("file") MultipartFile file, @RequestParam("token") String token, @RequestParam("photo") String photo) {
        //1.检查token
        if (token.isBlank() || token.isEmpty()) {
            //没有token直接返回
            return Result.fail("用户未登录");
        }
        //2.检查头像文件
        if (file.isEmpty()) {
            //没有头像直接返回
            return Result.fail("上传头像为空");
        }
        //3.根据token查询userId
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + token);
        String userId = (String) entries.get("userId");
        //4.创建文件对象
        String filename = userId + "-" + System.currentTimeMillis() + ".png";
        Path path = Paths.get(FILE_PATH + "photo\\" + filename);
        File photoFile = new File(String.valueOf(path));
        //5.判断文件是否存在
        //5.1判断photo是否为空
        if (!StrUtil.isBlank(photo) && !StrUtil.isEmpty(photo)){
            if (Files.exists(Paths.get(FILE_PATH + "photo\\" + photo))) {
                try {
                    //6.文件存在则直接删除
                    Files.delete(Paths.get(FILE_PATH + "photo\\" + photo));
                } catch (IOException e) {
                    return Result.fail("头像存在，尝试删除失败");
                }
            }
        }
        try {
            //7.将文件保存到指定路径
            file.transferTo(photoFile);
            //8.更新用户头像信息
            usersMapper.updateUserPhoto(filename, Integer.valueOf(userId));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("头像上传失败");
        }
        return Result.ok(filename);
    }

    @PostMapping("/uploadImg")
    public Result uploadImg(@RequestParam("img") MultipartFile file){
        //1.判断图片是否为空
        if (file.isEmpty()) {
            return Result.fail("上传失败，请重试");
        }
        String fileName = file.getOriginalFilename().replaceAll(" ","");
        //2。创建文件对象
        Path path = Paths.get(FILE_PATH_BLOG + fileName);
        //3.判断是否已经上传过该图片，如果没有上传过则上传后返回地址
        if (!Files.exists(path)) {
            try {
                file.transferTo(path);
            } catch (IOException e) {
                return Result.fail("图片保存失败，请重试");
            }
        }
        return Result.ok(NETFILE_FREFIX + port + "/" + NETFILE_SUFFIX + fileName);
    }
}
