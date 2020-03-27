package com.accompany.order.controller.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
public class UserResVo {
    @ApiModelProperty(value = "管理员id")
    private Long id;
    @ApiModelProperty(value = "用户名称")
    private String username;
    @ApiModelProperty(value = "管理员密码")
    private String password;
    @ApiModelProperty(value = "是否为超级管理员 1：是 0：否" )
    private Boolean isSuperAdmin;
    @ApiModelProperty(value = "管理员名字")
    private String name;
    @ApiModelProperty(value = "入职时间")
    private Date createTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @ApiModelProperty(value = "是否离职")
    private Boolean isDel;
    @ApiModelProperty(value = "Token")
    private String token;
    @ApiModelProperty(value = "avatar")
    private String avatar;
    @ApiModelProperty(value = "roles")
    private List<String> roles;
}
