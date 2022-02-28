package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    void add(CheckGroup checkGroup, Integer[] checkItemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findItemIdsByGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] ids);

    void delete(Integer id);

    List<CheckGroup> findAll();

}
