package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author Jay
 * @date 2022/2/16 2:59 下午
 * 检查项服务
 **/
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //检查项 分页查询
    public PageResult findQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //完成分页查询，基于mybatis框架提供的分页助手插件完成
        //bug
//        if (queryString != null && queryString.length() > 0) {
//            PageHelper.startPage(1, pageSize);
//        } else {
//        }
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        return new PageResult(total,rows);
    }

    //根据id删除检查项
    public void deleteById(Integer id) {
        //判断当前检查项是否已经关联到检查组
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
            //当前检查项已经被关联到检查组里，不允许删除
            new RuntimeException("当前检查项被引用，不能删除");
        }
        checkItemDao.deleteById(id);
    }

    //根据Id查找检查项
    public CheckItem findById(Integer id) {
        return checkItemDao.findCheckItemById(id);
    }

    //更新检查项
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
