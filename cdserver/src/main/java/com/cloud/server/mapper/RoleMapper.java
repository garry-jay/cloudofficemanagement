package com.cloud.server.mapper;

import com.cloud.server.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id查询角色列表
     */
    List<Role> getRoles(Integer adminId);
}
