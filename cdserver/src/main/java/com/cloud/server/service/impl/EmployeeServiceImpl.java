package com.cloud.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.server.pojo.Employee;
import com.cloud.server.mapper.EmployeeMapper;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.pojo.ResPageBean;
import com.cloud.server.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    /**
     *获取所有员工（分页）
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */
    public ResPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope) {
        //开启分页
        Page<Employee> page=new Page<>(currentPage,size);
        IPage<Employee> employeeByPage = employeeMapper.getEmployeeByPage(page, employee, beginDateScope);
        ResPageBean resPageBean = new ResPageBean(employeeByPage.getTotal(),employeeByPage.getRecords());
        return resPageBean;
    }

    /**
     * 获取工号 (当前最大工号加一)
     * @return
     */
    public ResBean maxWorkId() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));
        return ResBean.success(null,String.format("%08d",Integer.parseInt(maps.get(0).get("max(workID)").toString())+1));
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    public ResBean addEmp(Employee employee) {
        //处理合同期限，保留两位小数
        LocalDate beginContract = employee.getBeginContract();//获取合同开始时间
        LocalDate endContract = employee.getEndContract();//获取合同结束时间
        long days = beginContract.until(endContract, ChronoUnit.DAYS);//得到日期相差天数
        DecimalFormat decimalFormat = new DecimalFormat("##.00");//设置保留两位小数
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days/365.00)));
        if(1==employeeMapper.insert(employee))
        {
            return ResBean.success("添加成功！");
        }
        return ResBean.error("添加失败！");
    }

    /**
     * 查询员工 传了id就只查一条，不传id差所有
     */
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);
    }
}
