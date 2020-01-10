package com.accompany.order.controller.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class OrderItemReqVo {
    @ApiModelProperty(value = "商品id")
    private Long footId;

    @ApiModelProperty(value = "商品总数")
    private Integer count;
}
