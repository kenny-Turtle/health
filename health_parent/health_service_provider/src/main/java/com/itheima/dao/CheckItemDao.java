package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @author Jay
 * @date 2022/2/16 3:00 下午
 */
public interface CheckItemDao {
    public void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition(String queryString);

    public long findCountByCheckItemId(Integer id);

    public void deleteById(Integer id);

    public CheckItem findCheckItemById(Integer id);

    public void edit(CheckItem checkItem);

    public List<CheckItem> findAll();
}
