package com.cloud.server.service;

import com.cloud.server.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.server.pojo.ResBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据用户id查询菜单列表
     * @return
     */
    List<Menu> getMenusByAdminId();

    /**
     * 根据角色获取菜单列表(角色与菜单表进行关联，并不是输入一个角色让他输出对应的菜单列表)
     * @return
     */
    List<Menu> getMenusWithRole();

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> getAllMenus();


}
