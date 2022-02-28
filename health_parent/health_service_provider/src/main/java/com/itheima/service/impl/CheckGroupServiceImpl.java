package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Jay
 * @date 2022/2/18 9:44 上午
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {


    @Autowired
    private CheckGroupDao checkGroupDao;

    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        //新增检查组
        checkGroupDao.add(checkGroup);
        //设置检查组和检查项的关系，（新增关系表数据）
        setCheckGroupAndCheckItem(checkGroup.getId(), checkItemIds);
    }

    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> pages = checkGroupDao.selectByCondition(queryString);
        return new PageResult(pages.getTotal(),pages.getResult());
    }

    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    public List<Integer> findItemIdsByGroupId(Integer id) {
        return checkGroupDao.findItemIdsByGroupId(id);
    }

    public void edit(CheckGroup checkGroup, Integer[] ids) {
        //更新检查组
        checkGroupDao.edit(checkGroup);
        //先删，后更新关系表
        checkGroupDao.deleteAssociation(checkGroup.getId());
        setCheckGroupAndCheckItem(checkGroup.getId(), ids);
    }

    public void delete(Integer id) {
        //先查询该检查组是否有被包含在体检套餐里
        Long count = checkGroupDao.selectCountById(id);
        if (count > 0) {
            throw new RuntimeException("当前检查组被引用，不能删除");
        } else {
            //删除 关系表里的数据
            checkGroupDao.deleteAssociation(id);
            //删除 检查组
            checkGroupDao.deleteById(id);
        }
    }

    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }


    private void setCheckGroupAndCheckItem(Integer id, Integer[] checkItemIds) {
        if (checkItemIds != null && checkItemIds.length > 0) {
            for (Integer checkItemid : checkItemIds) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put("checkgroup_id", id);
                map.put("checkitem_id", checkItemid);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
