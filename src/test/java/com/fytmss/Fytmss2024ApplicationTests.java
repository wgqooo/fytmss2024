package com.fytmss;

import com.fytmss.beans.base.UserBean;
import com.fytmss.mapper.base.UserBeanMapper;
import jakarta.annotation.Resource;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class Fytmss2024ApplicationTests {

    @Resource
    private UserBeanMapper userMapper;

    @Test
    void contextLoads() {
//        SimpleHash simpleHash = new SimpleHash("SHA256", "77777", System.currentTimeMillis()+"", 1);
//        System.out.println(simpleHash.getSalt());
//        System.out.println(simpleHash.toHex());
//        UserBean user = UserBean.builder()
//                .empNo("77777")
//                .empPassword(simpleHash.toHex())
//                .empSex("男")
//                .empBirthday(new Date())
//                .empMobile("13657911223")
//                .empAddress("黑龙江哈尔滨")
//                .empDept(1)
//                .enabled(1).build();
//        userMapper.insert(user);
    }

}
