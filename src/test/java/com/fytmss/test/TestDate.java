package com.fytmss.test;

import com.fytmss.common.utils.DateUtils;
import com.fytmss.common.utils.IpUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Test;

import org.joda.time.format.DateTimeFormatter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wgq
 * @create 2024/6/20-周四 17:31
 */
public class TestDate {

    @Test
    public void testIP(){
        //String ip = "192.168.31.38";
      // String ip = "220.248.12.158"; // IpRegion:上海
        String ip = "47.52.236.180"; // IpRegion:香港
//        String ip = "172.22.12.123"; // IpRegion:内网IP
//        String ip = "164.114.53.60"; // IpRegion:美国
        String ipRegion = IpUtils.getIpRegion(ip);
        System.out.println(ipRegion);
    }

    @Test
    public void testAtomicInteger(){
        AtomicInteger sessionCount = new AtomicInteger(0);
        System.out.println(sessionCount.incrementAndGet());//1
        System.out.println(sessionCount.getAndIncrement());//1
        System.out.println(sessionCount.get());//2
    }

    @Test
    public void testDeque(){
        Deque deque = new ArrayDeque() ;
        deque.push(1);
        deque.push(2);
        System.out.println(deque.removeFirst());
    }

    @Test
    public void testDate4(){
        System.out.println(new Date());
        Date date = DateUtils.stringToDate("2024-08-10 01:05:00", DateUtils.DATE_TIME_PATTERN);
        assert date != null;
        System.out.println(date.getTime());
    }

    @Test
    public void testDate3(){
        System.out.println(new Date());
        System.out.println(DateUtils.nextDay("2021-02-28"));
        String dateTime = "2024-07-04 09:00:00";
        String[] date_time = dateTime.split(" ");
        System.out.println(date_time[0]);
        System.out.println(date_time[1]);
    }


    @Test
    public void testDate2(){
        Date date = new Date();
        String dateTime = DateUtils.format(date, DateUtils.DATE_TIME_PATTERN);
        System.out.println(dateTime);
    }


//    @Test
//    public void testAddDate(){
//        LocalDate startDate = LocalDate.parse("2024-12-20");
//        LocalDate endDate = LocalDate.parse("2025-03-05");
//
//        List<String> datesInRange = new ArrayList<>();
//        LocalDate date = startDate;
//        while (!date.isAfter(endDate)) {
//
//            datesInRange.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//            date = date.plusDays(1);
//        }
//        System.out.println(datesInRange);
//
//    }

    @Test
    public void testDate(){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse("2024-06-20", formatter);
        LocalDate endDate = LocalDate.parse("2024-07-05", formatter);

        List<String> datesInRange = new ArrayList<>();
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            datesInRange.add(date.toString(formatter));
            date = date.plusDays(1);
        }

        System.out.println(datesInRange);
    }
}
