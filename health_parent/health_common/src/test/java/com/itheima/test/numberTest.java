package com.itheima.test;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Jay
 * @date 2022/2/21 8:09 下午
 */
public class numberTest {



    //判断是不是小数
    @Test
    public void test1() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfMonth = Calendar.DAY_OF_MONTH;
        int i = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(dayOfMonth);
        System.out.println(i);
    }

    @Test
    public void getDateEnd() {
        String date = "2021.03";
        String[] split = date.split("\\.");
        Integer year = Integer.parseInt(split[0]);
        Integer month = Integer.parseInt(split[1]);
        if (month % 2 == 1) {
            System.out.println("31");
        } else if (month == 2) {
            if (year % 4 == 0) {
                System.out.println("29");
            } else System.out.println("28");
        } else {
            System.out.println("30");
        }
    }
}
