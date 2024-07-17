package com.fytmss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Fytmss2024Application {

//    @Resource
//    private UserMapper userMapper;

    public static void main(String[] args) {
        SpringApplication.run(Fytmss2024Application.class, args);
    }

//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        String salt = UUID.randomUUID().toString().replace("-","");
//        Sha256Hash sha256Hash = new Sha256Hash("77777", salt, 3);
//        User user = User.builder()
//                .empNo("77777")
//                .empPassword(sha256Hash.toHex())
//                .empSex("男")
//                .empBirthday(new Date())
//                .empMobile("13657911223")
//                .empAddress("黑龙江哈尔滨")
//                .empDept(1)
//                .enabled(1)
//                .empSalt(salt)
//                .build();
//        System.out.println(sha256Hash.getSalt());
//        userMapper.insert(user);
//    }
}
