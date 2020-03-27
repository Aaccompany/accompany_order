package com.accompany.order.service.revenue.dao;

import com.accompany.order.service.revenue.dto.RevenueDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@Mapper
public interface RevenueMapper  {

    @Select({
        "SELECT " +
            "DATE_FORMAT(create_time,'%d') AS 'day'," +
            "SUM(money) AS 'money' " +
            "FROM `order` " +
            "WHERE DATE_FORMAT(create_time,'%m')= #{month,jdbcType=INTEGER} " +
            "GROUP BY DATE_FORMAT(create_time,'%d') " +
            "ORDER BY DATE_FORMAT(create_time,'%d') "
    })
    @Results({
        @Result(column="day", property="day", jdbcType= JdbcType.INTEGER ),
        @Result(column="money", property="money", jdbcType=JdbcType.INTEGER)
    })
    List<RevenueDay> findDayRevenueOnMonth(int month);

    @Select({
        "SELECT " +
            "DATE_FORMAT(create_time,'%m') AS 'Month'" +
            ",SUM(money) as money " +
            "FROM `order` " +
            "WHERE DATE_FORMAT(create_time,'%Y') = #{month,jdbcType=INTEGER} " +
            "GROUP BY DATE_FORMAT(create_time,'%m')"
    })
    @Results({
        @Result(column="month", property="day", jdbcType= JdbcType.INTEGER),
        @Result(column="money", property="money", jdbcType=JdbcType.INTEGER)
    })
    List<RevenueDay> findMonthRevenueOnYear(int year);

}
