package com.accompany.order.service.revenue;

import com.accompany.order.config.SecurityInterceptor;
import com.accompany.order.exception.BaseRuntimeException;
import com.accompany.order.service.revenue.dao.RevenueMapper;
import com.accompany.order.service.revenue.dto.RevenueDay;
import com.accompany.order.service.user.UserServiceImpl;
import com.accompany.order.service.user.dto.User;
import com.accompany.order.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 收入管理
 */
@Service
public class RevenueServiceImpl  {

    @Resource
    private RevenueMapper revenueMapper;

    @Resource
    private UserServiceImpl userService;

    public List<RevenueDay> findDayRevenueOnMonth(int month) {
        if (month<=0||month>12){
            throw new BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("月份参数不合法，请检查后重新输入"));
        }
        if (userService.getCurUser()==null){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("没有查看权限，请登入后重试！"));
        }
        return revenueMapper.findDayRevenueOnMonth(month);
    }

    public List<RevenueDay> findMonthRevenueOnYear(int year){
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        if (year <= 1900 || year>curYear){
            throw new BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("月份参数不合法，请检查后重新输入"));
        }
        if (userService.getCurUser()==null){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("没有查看权限，请登入后重试！"));
        }
        return revenueMapper.findMonthRevenueOnYear(year);
    }

}
