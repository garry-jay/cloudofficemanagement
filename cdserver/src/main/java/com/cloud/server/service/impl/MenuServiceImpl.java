package com.cloud.server.service.impl;

import com.cloud.server.AdminUtils;
import com.cloud.server.pojo.Admin;
import com.cloud.server.pojo.Menu;
import com.cloud.server.mapper.MenuMapper;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 根据用户id查询菜单列表
     * @return
     */
    public List<Menu> getMenusByAdminId() {
        Integer adminId = AdminUtils.getCurrentAdmin().getId();
        //从一个 Redis 模板 (redisTemplate) 中获取一个用于操作字符串值的 ValueOperations 对象
        ValueOperations<String, Object> ValueOperations = redisTemplate.opsForValue();
        //从redis获取菜单数据
        List<Menu> menus = (List<Menu>) ValueOperations.get("menu_" + adminId);
        //判断menus是不是为空
        if(CollectionUtils.isEmpty(menus)){
            //如果redis里面没用就从mysql里面查
            menus=menuMapper.getMenusByAdminId(adminId);
            //将数据设置到redis
            ValueOperations.set("menu_"+adminId,menus);
        }
        return menus;
    }

    /**
     * 根据角色获取菜单列表
     * @return
     */
    public List<Menu> getMenusWithRole() {
        return menuMapper.getMenusWithRole();
    }

    /**
     * 查出所有菜单
     * @return
     */
    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }



}
