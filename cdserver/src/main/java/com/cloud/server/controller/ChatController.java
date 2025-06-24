package com.cloud.server.controller;

import com.cloud.server.pojo.Admin;
import com.cloud.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 在线聊天
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
     @Autowired
     private IAdminService adminService;

     @ApiOperation(value="获取所有操作员")
     @GetMapping("/")
     public List<Admin> getAllAdmins(String keywords){
         return adminService.getAllAdmins(keywords);
     }
}
