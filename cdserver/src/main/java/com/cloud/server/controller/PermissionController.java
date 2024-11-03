package com.cloud.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.server.pojo.*;
import com.cloud.server.service.IMenuRoleService;
import com.cloud.server.service.IMenuService;
import com.cloud.server.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissionController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value="获取所有角色")
    @GetMapping("/")
    public List<Role> getAllJobLevel(){
        return roleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/role")
    public ResBean addRole(@RequestBody Role role){
        if(!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }
        if(roleService.save(role)){
            return ResBean.success("添加成功！");
        }
        return ResBean.error("添加失败！");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public ResBean deleteRole(@PathVariable Integer rid){
        if(roleService.removeById(rid)){
            return ResBean.success("删除成功！");
        }
        return ResBean.error("删除失败！");
    }
    
    @ApiOperation(value="查询所有菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    //因为上面所有菜单都拿到了，所以这里只用返回菜单id
    @ApiOperation(value="根据角色id查询菜单id")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid){
        //通过stream流将list<MenuRole>转为了List<Integer>
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid",rid)).stream()
                .map(MenuRole::getMid).collect(Collectors.toList());
    }

    @ApiOperation(value="更新角色菜单")
    @PutMapping("/")
    public ResBean updateMenuRole(Integer rid,Integer[] mids){
        return menuRoleService.updateMenuRole(rid,mids);
    }
}
