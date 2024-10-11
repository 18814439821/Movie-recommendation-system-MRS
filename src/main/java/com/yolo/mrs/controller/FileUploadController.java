package com.yolo.mrs.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.yolo.mrs.mapper.UsersMapper;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IUsersService;
import com.yolo.mrs.service.impl.UsersServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yolo.mrs.utils.Constant.FILE_PATH;
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
}
