package com.fytmss.test;

import org.junit.jupiter.api.Test;

/**
 * @author wgq
 * @create 2024/6/16-周日 17:47
 */
public class TestScore {


    @Test
    public void test (){
        int[] arr = {
                63,
                61,
                88,
                71,
                71,
                78,
                75,
                78,
                54,
                68,
                78,
                82,
                78,
                71,
                82,
                71,
                82,
                74,
                83,
                81,
                81,
                78,
                80,
                67,
                72,
                65,
                70,
                79,
                83,
                85
        };
        int sum = 0;
        int c90 = 0;
        int c80 = 0;
        int c70 = 0;
        int c60 = 0;
        int c50 = 0;
        int max = 0;
        int min = 100;
        for (int i : arr) {
            max = Math.max(max, i);
            min = Math.min(min, i);
            sum += i;
            if(i >= 90){
                c90++;
            }else if(i >= 80){
                c80++;
            }else if(i >= 70){
                c70++;
            }else if(i >= 60){
                c60++;
            }else c50++;
        }

        System.out.println(c90);
        System.out.println(c80);
        System.out.println(c70);
        System.out.println(c60);
        System.out.println(c50);
        System.out.println(sum);
        System.out.println(max);
        System.out.println(min);

        int [][]arr1 = {{24,22},
                {24,24},
                {46,42},
                {30,32},
                {34,27},
                {38,33},
                {32,35},
                {32,38},
                {24,9},
                {30,27},
                {32,42},
                {42,35},
                {36,40},
                {28,30},
                {40,42},
                {36,25},
                {36,41},
                {32,31},
                {40,42},
                {38,38},
                {40,34},
                {38,33},
                {36,37},
                {24,34},
                {28,37},
                {28,21},
                {34,23},
                {36,36},
                {40,40},
                {36,45}
        };
        int s1 = 0,s2 = 0;
        for (int[] ints : arr1) {
            s1 += ints[0];
            s2 += ints[1];
        }
        System.out.println(s1);
        System.out.println(s2);
    }
}
