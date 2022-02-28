package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Jay
 * @date 2022/2/21 3:58 下午
 * 预约设置服务
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    public void add(List<OrderSetting> settings) {
        //预约服务是某天里面有多少预约，
        //如果库里有了某天的数据，用户还上传了这一天的数据，那就更新。
        //如果库里又没这天的数据，那就新增
        if (settings != null && settings.size() > 0) {
            for (OrderSetting setting : settings) {
                //查询此数据（日期）是否存在
                Long count = orderSettingDao.findCountByOrderDate(setting.getOrderDate());
                if (count > 0) {
                    //已经存在，执行更新操作
                    orderSettingDao.updateNumberByOrderDate(setting);
                } else {
                    //不存在则执行新增操作
                    orderSettingDao.add(setting);
                }
            }
        }
    }

    public List<Map> getOrderSettingByMonth(String date) {
        //2022-2
        //补上月初和月末
        String dateBegin = date + "-1";
        //闰年2月28天，非闰年29天
        String dateEnd = date + "-" + DateUtils.getDateEnd(date);
        Map map = new HashMap();
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<Map>();
        Calendar calendar = Calendar.getInstance();
        for (OrderSetting setting : list) {
            Map orderSettingMap = new HashMap();
            //获取当前预约时间 为当前月的几号
            calendar.setTime(setting.getOrderDate());
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            orderSettingMap.put("date", day);
            orderSettingMap.put("number", setting.getNumber());
            orderSettingMap.put("reservations", setting.getReservations());
            data.add(orderSettingMap);
        }
        return data;
    }

    public void editNumberByDate(OrderSetting orderSetting) {
        //先根据日期查询数据库看看是否有数据，有则更新，无则新增
        Long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            orderSettingDao.updateNumberByOrderDate(orderSetting);
        } else {
            orderSettingDao.add(orderSetting);
        }
    }


}
