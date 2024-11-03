package com.cloud.server.service;

import com.cloud.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.server.pojo.ResBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    ResBean updateMenuRole(Integer rid, Integer[] mids);
}
