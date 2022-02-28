package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jay
 * @date 2022/2/21 3:45 下午
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {


    @Reference
    private OrderSettingService orderSettingService;

    /*
     * 文件上传
     * */
    @PostMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile multipartFile) {
        try {
            //读取Excel文件数据
            List<String[]> list = POIUtils.readExcel(multipartFile);
            if (list != null && list.size() > 0) {
                List<OrderSetting> orderSettings = new ArrayList<OrderSetting>();
                for (String[] strings : list) {
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    orderSettings.add(orderSetting);
                }
                orderSettingService.add(orderSettings);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    /*
     * 根据月份来获取预约信息
     * */
    @PostMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {  //  2022-2
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /*
     * 根据日期设置可预约人数
     * */
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }







}
