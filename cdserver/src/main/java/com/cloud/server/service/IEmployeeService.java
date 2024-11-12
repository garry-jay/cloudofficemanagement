package com.cloud.server.service;

import com.cloud.server.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.pojo.ResPageBean;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
public interface IEmployeeService extends IService<Employee> {
    /**
     *获取所有员工（分页）
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */

    ResPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    /**
     * 获取工号
     * @return
     */
    ResBean maxWorkId();

    /**
     * 添加员工
     * @param employee
     * @return
     */
    ResBean addEmp(Employee employee);
}
