package com.fytmss.test;

import com.fytmss.common.utils.DateUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Test;

import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wgq
 * @create 2024/6/20-周四 17:31
 */
public class TestDate {

    @Test
    public void testDate3(){
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
