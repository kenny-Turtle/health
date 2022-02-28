package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jay
 * @date 2022/2/19 7:58 下午
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //新增套餐
        setmealDao.add(setmeal);
        //新增数据到关系表
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
        }
        //将图片名称保存到redis
        savePic2Redis(setmeal.getImg());
    }

    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> pages = setmealDao.selectByCondition(queryString);
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.edit(setmeal);
        setmealDao.deleteAssociation(setmeal.getId());
        setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
    }

    public void delete(Integer id) {
        //先删除表关系
        setmealDao.deleteAssociation(id);
        //删除套餐
        setmealDao.deleteById(id);
    }

    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    private void savePic2Redis(String img) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, img);
    }

    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupid : checkgroupIds) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("setmeal_id", id);
            map.put("checkgroup_id", checkgroupid);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }
}
