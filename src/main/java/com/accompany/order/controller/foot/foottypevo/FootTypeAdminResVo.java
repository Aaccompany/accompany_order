package com.accompany.order.controller.foot.foottypevo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class FootTypeAdminResVo {
    @ApiModelProperty(value = "类别id")
    private Long id;

    @ApiModelProperty(value = "类别名称")
    private String name;

    @ApiModelProperty(value = "类别描述")
    private String description;

    @ApiModelProperty(value = "创建者")
    private String createUser;

    @ApiModelProperty(value = "修改者")
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
