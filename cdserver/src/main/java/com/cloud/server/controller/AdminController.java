package com.cloud.server.controller;


import com.cloud.server.pojo.Admin;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value="获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmins(String keywords){
        return adminService.getAllAdmins(keywords);
    }

    @ApiOperation(value="更新操作员")
    @PutMapping("/")
    public ResBean updateAdmin(@RequestBody Admin admin){
        if(adminService.updateById(admin)){
            return ResBean.success("更新成功！");
        }
        else
            return ResBean.error("更新失败！");
    }

    @ApiOperation(value="删除操作员")
    @DeleteMapping("/{id}")
    public ResBean deleteAdmin(@PathVariable Integer id){
        if(adminService.removeById(id)){
            return ResBean.success("删除成功！");
        }
        return ResBean.error("删除失败！");
    }

}
