package com.cloud.server.controller;


import com.cloud.server.pojo.EmployeeEc;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.service.IEmployeeEcService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
@RequestMapping("personnel/ec/employee-ec")
public class EmployeeEcController {

    @Autowired
    private IEmployeeEcService employeeEcService;

    @ApiOperation(value="获取所有奖惩信息")
    @GetMapping("/")
    public List<EmployeeEc> getAllEmployeeEc(){
        return employeeEcService.list();
    }

    @ApiOperation(value="添加奖惩信息")
    @PostMapping("/")
    public ResBean addEmployeeEc(@RequestBody EmployeeEc employeeEc){
        employeeEc.setEcDate(LocalDate.now());
        if(employeeEcService.save(employeeEc)){
            return ResBean.success("添加成功！");
        }
        return ResBean.success("添加失败！");
    }

    @ApiOperation(value="删除一条奖惩记录")
    @DeleteMapping("/{id}")
    public ResBean deleteEmployeeEc(@PathVariable Integer id){
        if(employeeEcService.removeById(id)){
            return ResBean.success("成功删除!");
        }
        return ResBean.error("删除失败!");
    }

    @ApiOperation(value="删除一组奖惩记录")
    @DeleteMapping("/")
    public ResBean deleteEmployeeEcs(Integer [] ids){
        if(employeeEcService.removeByIds(Arrays.asList(ids))){
            return ResBean.success("成功删除！");
        }
        return ResBean.error("删除失败！");
    }

}


