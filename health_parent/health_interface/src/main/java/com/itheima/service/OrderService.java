package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

/**
 * @author Jay
 * @date 2022/2/23 9:36 上午
 */
public interface OrderService {

    Result order(Map map) throws Exception;

    Map findById(Integer id) throws Exception;

}
