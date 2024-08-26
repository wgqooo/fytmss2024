package com.fytmss.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fytmss.beans.base.RoleBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wgq
 * @create 2024/8/10-周六 0:18
 */
@SpringBootTest
public class TestJson {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test2() throws JsonProcessingException {
        // 配置 Redis 连接
//        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration("192.168.88.100", 6379);
//        RedisConnectionFactory connectionFactory = new LettuceConnectionFactory(redisConfig);
        // 创建 StringRedisTemplate 实例并配置连接工厂
       // StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        String s = stringRedisTemplate.opsForValue().get("voyage:2024-08-11:G2015");
        System.out.println(s);
    }

    @Test
    public void test1() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> map = new HashMap<>();
        map.put("wgq", 1);
        map.put("wjw", 2);
        List<RoleBean> list = new ArrayList<>();
        RoleBean roleBean = new RoleBean(1, "admin", "管理员");
        RoleBean roleBean2 = new RoleBean(2, "manager", "经理");
        list.add(roleBean);
        list.add(roleBean2);
        String s = objectMapper.writeValueAsString(list);
        System.out.println(s);
        System.out.println(list);
    }
}
