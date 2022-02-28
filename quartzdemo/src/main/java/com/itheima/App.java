package com.itheima;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jay
 * @date 2022/2/21 9:53 上午
 */
public class App {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-jobs.xml");
    }
}
