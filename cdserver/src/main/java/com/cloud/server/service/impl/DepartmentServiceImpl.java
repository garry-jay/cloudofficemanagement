package com.cloud.server.service.impl;

import com.cloud.server.pojo.Department;
import com.cloud.server.mapper.DepartmentMapper;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    /**
     * 获取所有部门
     * @return
     */
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartments(-1);
    }

    /**
     * 添加部门
     * @param dep
     * @return
     */
    public ResBean addDep(Department dep) {
        dep.setEnabled(true);
        departmentMapper.addDep(dep);
        if(1==dep.getResult()){
            return ResBean.success("添加成功",dep);
        }
        return ResBean.error("添加失败！");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    public ResBean deleteDep(Integer id) {
        Department dep=new Department();
        dep.setId(id);
        departmentMapper.deleteDep(dep);
        if(-2==dep.getResult()){
            return ResBean.error("该部门下还有子部门，删除失败！");
        }
        if(-1==dep.getResult()){
            return ResBean.error("该部门下还有员工，删除失败！");
        }
        if(1==dep.getResult()){
            return ResBean.success("删除成功！");
        }
        return ResBean.success("删除失败！");
    }
}
