package com.yolo.mrs;

import com.yolo.mrs.service.IMovieMidService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

import static com.yolo.mrs.utils.RedisConstant.GENRE_TYPE;

class MrsApplicationTest {

    @Resource
    IMovieMidService movieMidService;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    void userHolderTest() {
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(GENRE_TYPE))) {
            stringRedisTemplate.opsForZSet().add(GENRE_TYPE,"genre",0);
        }
    }
}