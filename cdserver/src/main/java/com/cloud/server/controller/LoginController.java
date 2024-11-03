package com.cloud.server.controller;

import com.cloud.server.pojo.Admin;
import com.cloud.server.pojo.AdminLoginParam;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登录
 */
@Api(tags="LoginController")
@RestController
public class LoginController {
    @Autowired
    private IAdminService adminService;
    @ApiOperation(value="登录之后返回token")
    @PostMapping("/login")
    public ResBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){
        return adminService.login(adminLoginParam.getUsername(),adminLoginParam.getPassword(),adminLoginParam.getCode(),request);
    }

    @ApiOperation(value="获取当前用户登陆的信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal){//SpringSecurity当前登陆的对象设置到全局里面去了，这里用Principal principal获取到当前登陆的一个对象
        if(null==principal){
            return null;
        }
        String username=principal.getName();
        Admin admin=adminService.getAdminByUserName(username);
        admin.setPassword(null);
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }
    @ApiOperation(value="退出登录")
    @PostMapping("/logout")
    public ResBean logout(){
        return ResBean.success("注销成功！");
    }
}
