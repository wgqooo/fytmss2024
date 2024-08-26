package com.fytmss.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wgq
 * @create 2024/5/15-周三 19:21
 */
public class CountAdd {

    public static void main(String[] args) {
        int c = 0;
        for (int i = 0; i < 100; i++) {
            c = c++;
        }
        System.out.println(c);

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        int[] arr = {2,3,5,6};
        for (int i : arr) {
            if(list.contains(i)){
                list.remove((Integer) i);
                System.out.println("common :" + i);
            }else{
                System.out.println("diff :" + i);
            }
        }
        for (int i : list) {
            System.out.println(i);
        }

        List<Integer> list2 = new ArrayList<>();
        for (Integer i : list2) {
            System.out.println(i);
        }
    }


}
