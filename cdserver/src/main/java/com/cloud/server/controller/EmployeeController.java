package com.cloud.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.cloud.server.pojo.*;
import com.cloud.server.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
//@RequestMapping("/system/basic/employee")
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
    @GetMapping("/politicsStatus")
    public List<PoliticsStatus> getAllPoliticsStatus(){
        return politicsStatusService.list();
    }

    @ApiOperation(value="获取所有职称")
    @GetMapping("/joblevel")
    public List<Joblevel> getAllJoblevels(){
        return joblevelService.list();
    }

    @ApiOperation(value="获取所有民族")
    @GetMapping("/nation")
    public List<Nation> getAllNations(){
        return nationService.list();
    }

    @ApiOperation(value="获取所有职位")
    @GetMapping("/position")
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
        //第一个参数是标题、第二个参数是表名，第三个参数是文件后缀 xls
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

    @ApiOperation(value="导入员工数据")
    @PostMapping("/import")

    public ResBean importEmployee(MultipartFile file){
        ImportParams params =new ImportParams();
        //去掉标题行
        params.setTitleRows(1);
        List<Nation> nationList = nationService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        List<Department> departmentList = departmentService.list();
        List<Joblevel> joblevelList = joblevelService.list();
        List<Position> positionList = positionService.list();
        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            list.forEach(employee -> {
                //通过nationlist获取索引下标，因为我重载了hashcode和equal，现在他用name去比较了,这样就只查一个数据库，不然的话，你每一个name都去查数据库
                //设置民族id
                employee.setNationId(nationList.get(nationList.indexOf(new Nation(employee.getNation().getName()))).getId());
                //设置政治面貌id
                employee.setPoliticId(politicsStatusList.get(politicsStatusList.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
                //设置部门id
                employee.setDepartmentId(departmentList.get(departmentList.indexOf(new Department(employee.getDepartment().getName()))).getId());
                //职称id
                employee.setJobLevelId(joblevelList.get(joblevelList.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId());
                //职位id
                employee.setPosId(positionList.get(positionList.indexOf(new Position(employee.getPosition().getName()))).getId());

            });
            if(employeeService.saveBatch(list)){
                return ResBean.success("导入成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResBean.error("导入失败！");
    }

}
