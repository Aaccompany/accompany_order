package com.accompany.order.controller.foot.foottypevo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class FootTypeAdminReqVo {
    @ApiModelProperty(value = "类别名称")
    private String name;

    @ApiModelProperty(value = "类别描述")
    private String description;
}
