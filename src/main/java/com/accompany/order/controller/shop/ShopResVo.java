package com.accompany.order.controller.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class ShopResVo {
    @ApiModelProperty(value = "店面id")
    private Long id;

    @ApiModelProperty(value = "店面名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;
}
