package com.cloud.server.mapper;

import com.cloud.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.server.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {


    /**
     * 获取所有操作员
     * @param id
     * @param keywords
     * @return
     */
    //有两个参数了，我们加一个Param注解
    List<Admin> getAllAdmins(@Param("id") Integer id, @Param("keywords") String keywords);
}
