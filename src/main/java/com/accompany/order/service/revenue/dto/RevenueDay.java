package com.accompany.order.service.revenue.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
  public class RevenueDay implements Serializable {

    private static final long serialVersionUID=1L;

    private int day;

    private double money;

}
