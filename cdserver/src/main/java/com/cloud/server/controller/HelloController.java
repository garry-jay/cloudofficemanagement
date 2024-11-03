package com.cloud.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 */
@RestController
public class HelloController {
    @GetMapping("hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/employee/basic/**")
    public String hello2(){
        return "/employee/basic/**";
    }

    @GetMapping("/employee/advanced/hello")
    public String hello3(){
        return "/employee/advanced/hello";
    }
}
