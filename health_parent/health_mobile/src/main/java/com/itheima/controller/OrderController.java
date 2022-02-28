package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.qiniu.storage.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.security.cert.TrustAnchor;
import java.util.Map;

/**
 * @author Jay
 * @date 2022/2/23 9:05 上午
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /*
     * 提交预约信息
     * */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map map) {
        //验证用户提交的验证码对不对（与redis中的验证码比较）
        String telephone = (String) map.get("telephone");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String code = (String) map.get("validateCode");
        if (code == null || !codeInRedis.equals(code)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //保存到数据库
        //调用服务
        Result result = null;
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        //预约成功，发送短信通知
        if (result.isFlag()) {
            //发送短信
        }
        return result;
    }

    /*
     * 根据id查找预约信息,包括体检人信息，套餐信息
     * */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
