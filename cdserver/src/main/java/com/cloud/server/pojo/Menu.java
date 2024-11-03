package com.cloud.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Menu对象", description="")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "path")
    private String path;

    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "菜单名")
    private String name;

    @ApiModelProperty(value = "图标")
    private String iconCls;

    @ApiModelProperty(value = "是否保持激活")
    private Boolean keepAlive;

    @ApiModelProperty(value = "是否要求权限")
    private Boolean requireAuth;

    @ApiModelProperty(value = "父id")
    private Integer parentId;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    //返回的时候也会返回子菜单，因此这里 添加一个子菜单的属性
    @ApiModelProperty(value = "子菜单")
    @TableField(exist = false)//因为使用的是mybatis-plus，所以这里要说明表字段没这个东西
    private List<Menu> children;

    //根据访问的url判断角色
    @ApiModelProperty(value="角色列表")
    @TableField(exist = false)
    private List<Role> roles;
}
