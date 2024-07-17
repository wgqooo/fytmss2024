package com.fytmss.test;

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
    }
}
