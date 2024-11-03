package com.cloud.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.server.pojo.MenuRole;
import com.cloud.server.mapper.MenuRoleMapper;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
@Service

public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;
    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    //因为分两步来处理，所以加上Transactional注解保证一致性
    @Transactional
    public ResBean updateMenuRole(Integer rid, Integer[] mids) {
        //removeById是根据主键id删除，这里是根据角色id删除
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
        //处理mids为空的情况
        if(mids==null||mids.length==0){
            return ResBean.success("更新成功！");
        }
        //批量更新
        Integer result = menuRoleMapper.insertRecore(rid, mids);
        if(result==mids.length){
            return ResBean.success("更新成功！");
        }
        return ResBean.error("更新失败！");
    }
}
