package com.accompany.order.controller.foot.footvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class FootAdminResVo {
    @ApiModelProperty(value = "菜品id")
    private Long id;

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

    @ApiModelProperty(value = "创建者")
    private Long createUser;

    @ApiModelProperty(value = "修改者")
    private Long updateUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
