package com.accompany.order.controller.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class OrderItemAdminResVo {
    @ApiModelProperty(value = "订单项id")
    private Long id;

    @ApiModelProperty(value = "商品id")
    private Long footId;

    @ApiModelProperty(value = "父订单id")
    private Long orderId;

    @ApiModelProperty(value = "商品名称")
    private String footName;

    @ApiModelProperty(value = "商品总数")
    private Integer count;

    @ApiModelProperty(value = "订单项总价")
    private Integer money;

    @ApiModelProperty(value = "商品单价")
    private Integer unitPrice;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
