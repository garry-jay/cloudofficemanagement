package com.cloud.server.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cloud.server.pojo.Employee;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.pojo.ResPageBean;
import com.cloud.server.pojo.Salary;
import com.cloud.server.service.IEmployeeService;
import com.cloud.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工账套
 */
@RestController
@RequestMapping("/salary/sobcfg")
public class SalarySobCfgController {
    @Autowired
    private ISalaryService salaryService;
    @Autowired
    private IEmployeeService employeeService;

    //由于这一个页面也有有获取所有工资账套的的操作，所以这里也要再写一遍
    @ApiOperation(value="获取所有工资账套")
    @GetMapping("/salaries")
    public List<Salary> getAllSalaries(){
        return salaryService.list();
    }

    @ApiOperation(value="获取所有员工账套")
    @GetMapping("/")
    public ResPageBean getEmployeeWithSalary(@RequestParam(defaultValue = "1")Integer currentPage,@RequestParam(defaultValue = "10") Integer size){
        return employeeService.getEmployeeWithSalary(currentPage,size);
    }

    @ApiOperation(value="更新员工账套")
    @PutMapping("/")
    public ResBean updateEmployeeSalary(Integer eid,Integer sid){//员工id与salary id
        if(employeeService.update(new UpdateWrapper<Employee>().set("salaryId",sid).eq("id",eid))){
            return ResBean.success("更新成功！");
        }
        return ResBean.error("更新失败！");
    }

}
