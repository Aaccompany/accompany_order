package com.accompany.order.controller.foot.footvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class FootAdminReqVo {
    @ApiModelProperty(value = "菜品名称")
    private String name;

    @ApiModelProperty(value = "菜品描述")
    private String description;

    @ApiModelProperty(value = "菜品类别")
    private Long typeId;

    @ApiModelProperty(value = "钱")
    private Long money;

    @ApiModelProperty(value = "食物照片")
    private String picture;
}
