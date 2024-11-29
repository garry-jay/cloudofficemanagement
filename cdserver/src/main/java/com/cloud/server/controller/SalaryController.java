package com.cloud.server.controller;


import com.cloud.server.pojo.ResBean;
import com.cloud.server.pojo.Salary;
import com.cloud.server.service.ISalaryService;
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
//工资账套的作用是定一个基本工资模板
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {
    @Autowired
    private ISalaryService salaryService;
    @ApiOperation(value="获取所有工资账套")
    @GetMapping("/")
    public List<Salary> getAllSalaries(){
        return salaryService.list();
    }


    @ApiOperation(value="添加工资账套")
    @PostMapping("/")
    public ResBean addSalary(@RequestBody Salary salary){
        if(salaryService.save(salary)){
            return ResBean.success("添加成功！");
        }
        return ResBean.error("添加失败！");
    }


    @ApiOperation(value="删除工资账套")
    @DeleteMapping("/{id}")
    public ResBean deleteSalary(@PathVariable Integer id){
        if(salaryService.removeById(id)){
             return ResBean.success("删除成功！");
        }
        return ResBean.error("删除失败！");
    }


    @ApiOperation(value="更新工资账套")
    @PutMapping("/")
    public ResBean updateSalary(@RequestBody Salary salary){
        if(salaryService.updateById(salary)){
            return ResBean.success("更新成功！");
        }
        return ResBean.error("更新失败！");
    }
}
