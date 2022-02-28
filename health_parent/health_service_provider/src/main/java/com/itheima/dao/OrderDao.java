package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    List<Order> findByCondition(Order order);

    void add(Order order);

    Map findById4Detail(Integer id);

    Integer findOrderCountByDate(String date);

    Integer findOrderCountAfterDate(String date);

    Integer findVisitsCountByDate(String date);

    Integer findVisitsCountAfterDate(String date);

    List<Map> findHotSetmeals();
}
