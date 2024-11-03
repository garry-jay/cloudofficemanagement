package com.cloud.server.controller;


import com.cloud.server.pojo.Department;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.service.IDepartmentService;
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
@RequestMapping("/system/basic/department")
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value="获取所有部门")
    @GetMapping("/")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments(-1);
    }

    @ApiOperation("添加部门")
    @PostMapping("/")
    public ResBean addDep(@RequestBody Department dep){
        return departmentService.addDep(dep);
    }

    @ApiOperation("删除部门")
    @DeleteMapping("/{id}")
    public ResBean deleteDep(@PathVariable Integer id){
        return departmentService.deleteDep(id);
    }
}
