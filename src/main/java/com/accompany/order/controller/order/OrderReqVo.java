package com.accompany.order.controller.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class OrderReqVo {

    @ApiModelProperty(value = "座位号")
    private Integer tableId;

    @ApiModelProperty(value = "订单项列表")
    private List<OrderItemReqVo> itmeList;
}
