package com.accompany.order.controller.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class UserLoginVo {
    @ApiModelProperty(value = "用户名称")
    private String username;
    @ApiModelProperty(value = "管理员密码")
    private String password;
}
