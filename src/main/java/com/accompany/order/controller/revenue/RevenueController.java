package com.accompany.order.controller.revenue;

import com.accompany.order.service.foot.FootServiceImpl;
import com.accompany.order.service.foot.dto.FootCountOnMonth;
import com.accompany.order.service.revenue.RevenueServiceImpl;
import com.accompany.order.service.revenue.dto.RevenueDay;
import com.accompany.order.util.Result;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/revenue")
@Api(tags = {"所有接口"})
public class RevenueController {

    @Resource
    private RevenueServiceImpl revenueService;

    @Resource
    private FootServiceImpl footService;

    @GetMapping(value = "/money/day/{month}")
    @ApiOperation(value = "获取一个月份下每天的营收情况(默认查询当前年份）",tags = {"管理员->收入管理"})
    public Result<List<RevenueDay>> findDayRevenueOnMonth(@PathVariable int month){
        return Result.success(revenueService.findDayRevenueOnMonth(month));
    }

    @GetMapping(value = "/money/month/{year}")
    @ApiOperation(value = "获取一年的每月营收情况(默认查询当前年份）",tags = {"管理员->收入管理"})
    public Result<List<RevenueDay>> findMonthRevenueOnYear(@PathVariable int year){
        return Result.success(revenueService.findMonthRevenueOnYear(year));
    }

    @GetMapping("/foot/month/{month}")
    @ApiOperation(value = "获取一个月份下菜品的售卖情况(默认查询当前年份）",tags = {"管理员->收入管理"})
    public Result<List<FootCountVo>> findCountOnMonth(@PathVariable int month){
        List<FootCountOnMonth> countOnMonth = footService.findCountOnMonth(month);
        List<FootCountVo> list = Lists.newArrayList();
        for (FootCountOnMonth footCountOnMonth : countOnMonth) {
            FootCountVo footCountVo = new FootCountVo();
            footCountVo.setName(footCountOnMonth.getFootName());
            footCountVo.setValue(footCountOnMonth.getCount());
            list.add(footCountVo);
        }
        return Result.success(list);
    }
}
