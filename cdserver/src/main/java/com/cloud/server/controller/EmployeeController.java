package com.cloud.server.controller;


import com.cloud.server.pojo.Employee;
import com.cloud.server.pojo.ResPageBean;
import com.cloud.server.service.IEmployeeEcService;
import com.cloud.server.service.IEmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {
    @Autowired
     private IEmployeeService employeeService;
    @ApiOperation(value="获取所有员工（分页）")
    @GetMapping("/")
    public ResPageBean getEmployee(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size, Employee employee, LocalDate[] beginDateScope){
        return employeeService.getEmployeeByPage(currentPage,size,employee,beginDateScope);
    }
}
