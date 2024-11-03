package com.cloud.server.service.impl;

import com.cloud.server.pojo.Employee;
import com.cloud.server.mapper.EmployeeMapper;
import com.cloud.server.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
