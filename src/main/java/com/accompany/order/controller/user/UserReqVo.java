package com.accompany.order.controller.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class UserReqVo {
    @ApiModelProperty(value = "管理员密码")
    private String password;
    @ApiModelProperty(value = "是否为超级管理员 1：是 0：否" )
    private Boolean isSuperAdmin;
    @ApiModelProperty(value = "用户名称")
    private String username;
    @ApiModelProperty(value = "管理员名字")
    private String name;
}
