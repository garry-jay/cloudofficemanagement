package com.cloud.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.cloud.server.pojo.*;
import com.cloud.server.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
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
@RequestMapping("/employee/basic")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IPoliticsStatusService politicsStatusService;
    @Autowired
    private IJoblevelService joblevelService;
    @Autowired
    private INationService nationService;
    @Autowired
    private IPositionService positionService;
    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value="获取所有员工（分页）")
    @GetMapping("/")
    public ResPageBean getEmployee(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size, Employee employee, LocalDate[] beginDateScope){
        return employeeService.getEmployeeByPage(currentPage,size,employee,beginDateScope);
    }

    @ApiOperation(value="获取所有政治面貌")
    @GetMapping("/politicsstatus")
    public List<PoliticsStatus> getAllPoliticsStatus(){
        return politicsStatusService.list();
    }

    @ApiOperation(value="获取所有职称")
    @GetMapping("/joblevels")
    public List<Joblevel> getAllJoblevels(){
        return joblevelService.list();
    }

    @ApiOperation(value="获取所有民族")
    @GetMapping("/nations")
    public List<Nation> getAllNations(){
        return nationService.list();
    }

    @ApiOperation(value="获取所有职位")
    @GetMapping("/positions")
    public List<Position> getAllPositions(){
        return positionService.list();
    }

    @ApiOperation(value="获取所有部门")
    @GetMapping("/deps")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @ApiOperation(value="获取工号")
    @GetMapping("/maxWorkID")
    public ResBean maxWorkId(){
        return employeeService.maxWorkId();
    }

    @ApiOperation(value="添加员工")
    @PostMapping("/")
    public ResBean addEmp(@RequestBody Employee employee) {
        return employeeService.addEmp(employee);
    }

    @ApiOperation(value="更新员工")
    @PutMapping("/")
    public ResBean updateEmp(@RequestBody Employee employee){
        if(employeeService.updateById(employee)) {
            return ResBean.success("更新成功!");
        }
        return ResBean.error("更新失败！");
    }

    @ApiOperation(value="删除员工")
    @DeleteMapping("/{id}")
    public ResBean deleteEmp(@PathVariable Integer id){
        if(employeeService.removeById(id)){
            return ResBean.success("删除成功！");
        }
        return ResBean.error("删除失败!");
    }

    @ApiOperation(value = "导出员工数据")
    @GetMapping(value="/export",produces = "application/octet-stream")//produces规定以流的形式输出
    public void exportEmployee(HttpServletResponse response){//以流的形式导出要用这个：HttpServletResponse response
        List<Employee> list = employeeService.getEmployee(null);
        //第一个参数是文件名、第二个参数是表名，第三个参数是文件后缀 xls
        ExportParams params=new ExportParams("员工表","员工表", ExcelType.HSSF);
        //第二个参数是对象
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class, list);
        ServletOutputStream out=null;
        try {
            //以流的形式传出
            response.setHeader("content-type","application/octet-stream");
            //防止中文乱码
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("员工表.xls","UTF-8"));
            out = response.getOutputStream();
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=out){
                try {
                    //关闭流
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
