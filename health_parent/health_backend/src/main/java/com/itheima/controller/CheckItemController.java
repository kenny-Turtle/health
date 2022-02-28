package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jay
 * @date 2022/2/16 2:42 下午
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    /*
     * 新增检查项
     * *为什么要加@RequestBody？
     * *因为前端传过来的是json格式的数据，springmvc是不能自动的帮我们转成实体类的，所以需要加上这个配置
     * */
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);

        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /*
     * 检查项 分页查询
     * */
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")//权限校验
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.findQuery(queryPageBean);
        return pageResult;
    }

    /*
     * 删除检查项
     * */
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限校验
    @GetMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkItemService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /*
     * 根据ID查找检查项
     * */
    @GetMapping("/findById")
    public Result findById(Integer id) {
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /*
     * 更新检查项
     * */
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    @PostMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /*
     * 查询所有检查项
     * */
    @GetMapping("/findAll")
    public Result findAll() {
        List<CheckItem> datas = checkItemService.findAll();
        if (datas != null && datas.size() > 0) {
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, datas);
        }
        return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
    }


}
