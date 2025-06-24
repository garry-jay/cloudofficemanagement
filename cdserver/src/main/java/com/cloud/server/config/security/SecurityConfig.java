package com.cloud.server.config.security;

import com.cloud.server.config.security.component.*;
import com.cloud.server.pojo.Admin;
import com.cloud.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Security配置类
 */
@Configuration //可以使用 @Bean 注解定义方法
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Autowired
    private RestfulAccessDeniedHand restfulAccessDeniedHand;
    @Autowired
    private CustomFilter customFilter;
    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
        //Spring Security会自动走我们的重写的userDetailsService
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncode());
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/login",
                "/logout",
                "/css/**",
                "/js/**",
                "index.html",
                "favicon,ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha",
                "/ws/**"
        );
    }
    protected void configure(HttpSecurity http) throws Exception {
        //使用jwt不需要csrf
        http.csrf()
                .disable()
                //基于token，不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //所有请求都要求认证
                .anyRequest()
                .authenticated()
                //动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                })
                .and()
                //禁用缓存
                .headers()
                .cacheControl();
        //添加jwt，登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        //新增 配置 CORS
//        http.cors().configurationSource(request -> {
//                    CorsConfiguration config = new CorsConfiguration();
//                    config.setAllowCredentials(true);  // 允许发送凭证（cookies）
//                    config.addAllowedOrigin("http://localhost:*");  // 替换为允许的域名
//                    config.addAllowedMethod("*");  // 允许所有请求方法
//                    config.addAllowedHeader("*");  // 允许所有请求头
//                    return config;
//                })
//                .and();
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHand)
                .authenticationEntryPoint(restAuthorizationEntryPoint);
    }



    //重写了userDetailsService方法
    //标记 userDetailsService 方法为一个 Spring bean。当 Spring 容器启动时，会调用这个方法，并将返回的 UserDetailsService 对象注册到 Spring 上下文中
    //重写了loadUserByUsername
    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        //username -> { ... }：这是一个 lambda 表达式，表示一个接受 username 参数的函数
        //这个 username 参数是在用户登录时由 Spring Security 自动传递过来的
        return username -> {
            Admin admin = adminService.getAdminByUserName(username);
            if(admin!=null){
                admin.setRoles(adminService.getRoles(admin.getId()));
                return admin;
            }
            throw new UsernameNotFoundException("用户名或密码不正确");
        };
    }

    @Bean
    public PasswordEncoder passwordEncode(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}
