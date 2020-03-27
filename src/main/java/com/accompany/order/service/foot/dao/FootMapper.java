package com.accompany.order.service.foot.dao;

import com.accompany.order.service.foot.dto.Foot;
import com.accompany.order.service.foot.dto.FootCountOnMonth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
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
public interface FootMapper extends BaseMapper<Foot> {

    @Select("SELECT  " +
        "SUM(count) as 'count' ," +
        " foot.`name` as 'footName' ," +
        "foot.id as 'footId' " +
        "FROM `order_item` INNER JOIN foot ON order_item.foot_id = foot.id " +
        "WHERE DATE_FORMAT(order_item.create_time,'%m')= #{month,jdbcType=INTEGER}  " +
        "GROUP BY order_item.foot_id " +
        "ORDER BY count desc LIMIT 4")
    @Results({
        @Result(column="count", property="count", jdbcType= JdbcType.INTEGER),
        @Result(column="footName", property="footName", jdbcType=JdbcType.VARCHAR),
        @Result(column="footId", property="footId", jdbcType=JdbcType.BIGINT)
    })
    public List<FootCountOnMonth> findCountOnMonth(int month);

    @Select("select SUM(count) from order_item WHERE DATE_FORMAT(create_time,'%m') = #{month,jdbcType=INTEGER}")
    int footCountOnMonth(int month);
}
