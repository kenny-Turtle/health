package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String,Integer> map);

    Page<Setmeal> selectByCondition(String queryString);

    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    Setmeal findById(Integer id);

    void edit(Setmeal setmeal);

    void deleteAssociation(Integer id);

    void deleteById(Integer id);

    List<Setmeal> findAll();

    List<Map<String,Object>> findSetmealCount();

}
