package com.cloud.server;

import com.cloud.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 操作员工具类
 */
public class AdminUtils {
    public static Admin getCurrentAdmin(){
        //获取当前登录操作员
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
