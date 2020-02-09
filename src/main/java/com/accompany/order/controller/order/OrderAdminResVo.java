package com.accompany.order.controller.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class OrderAdminResVo {
    @ApiModelProperty(value = "订单号")
    private Long id;

    @ApiModelProperty(value = "订单总价")
    private Double money;

    @ApiModelProperty(value = "座位号")
    private Long tableId;

    @ApiModelProperty(value = "是否结算 2:表示某种方式结算 1:表示结算 0：表示未结算")
    private Integer isPay;

    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "订单项列表")
    private List<OrderItemAdminResVo> itemList;
}
