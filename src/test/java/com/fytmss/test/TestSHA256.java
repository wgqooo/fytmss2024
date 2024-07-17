package com.fytmss.test;

import com.fytmss.common.utils.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author wgq
 * @create 2024/5/17-周五 20:23
 */
public class TestSHA256 {

    @Test
    public void testDate3() throws Exception{
        System.out.println(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
    }

    @Test
    public void testStr(){
        String s = "4d47a";
        System.out.println(s.substring(s.length() - 6));
    }

    @Test
    public void testMap(){
        Map<String, Integer> map = new HashMap<>();
        map.put("index",1);
        map.put("size",1);
        map.put("total",1);
        System.out.println(map.get("index"));
        int i = map.get("index");
        System.out.println(map);
    }

    @Test
    public void test2SHA256(){
        String birth = "2024-06-10";
        Sha256Hash sha256Hash = new Sha256Hash(birth.replace("-",""),"755640d0f8c04f658d643103764ea888",1);
        System.out.println(sha256Hash.toHex());
    }

    @Test
    public void testPwd(){
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
    }

    @Test
    public void testDate2(){
        String dateTimeString = "2000-12-31T16:00:00.000Z";

        // 解析日期时间字符串
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);

        // 转换为中国时区
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        dateTime = dateTime.atOffset(ZoneOffset.UTC).atZoneSameInstant(chinaZoneId).toLocalDateTime();

        // 格式化为目标格式
        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 输出结果
        System.out.println("原始日期时间字符串: " + dateTimeString);
        System.out.println("中国时间的日期字符串: " + formattedDate);
    }



    @Test
    public void testDate(){
        String dateTime = "2020-01-13T16:00:00.000Z";
        System.out.println(DateUtils.formatUTC(dateTime));
    }

    @Test
    public void getSubject(){
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject);
    }

    @Test
    public void testSHA(){
        String salt = UUID.randomUUID().toString().replace("-","");
        System.out.println(salt);
        SimpleHash simpleHash = new SimpleHash("SHA256", "77777", salt, 3);
        System.out.println(ByteSource.Util.bytes(salt));
        System.out.println(simpleHash.getSalt());
        System.out.println(simpleHash.toHex());
    }


    @Test
    public void testSHA256(){
        //算法名字配置SHA-256和SHA256没有区别
        String salt = UUID.randomUUID().toString();
        SimpleHash simpleHash1 = new SimpleHash("SHA-256", "77777");
        System.out.println(simpleHash1.getAlgorithmName());
        simpleHash1.setIterations(1);
        SimpleHash simpleHash2 = new SimpleHash("SHA256", "77777", salt, 1);
        System.out.println(simpleHash1.getSalt());
        System.out.println(simpleHash1.toHex());
        System.out.println(simpleHash2.getSalt());
        System.out.println(simpleHash2.toHex());

    }
}
