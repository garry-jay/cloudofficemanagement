package com.cloud.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.server.utils.AdminUtils;
import com.cloud.server.config.security.component.JwtTokenUtil;
import com.cloud.server.mapper.AdminRoleMapper;
import com.cloud.server.mapper.RoleMapper;
import com.cloud.server.pojo.Admin;
import com.cloud.server.mapper.AdminMapper;
import com.cloud.server.pojo.AdminRole;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.pojo.Role;
import com.cloud.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */

//ServiceImpl 类内部会自动注入 AdminMapper，所以在 AdminServiceImpl 中你不需要显式地注入它。
//MyBatis-Plus 会根据类名和类型找到相应的 Mapper，并自动处理 CRUD 操作。
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    /**
     * 登录之后返回token
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    public ResBean login(String username, String password, String code, HttpServletRequest request) {
        //获取啊session中的验证码
       String captcha = (String)request.getSession().getAttribute("captcha");
       //判断用户有没有输入验证码，或者判断session中的验证码和用户输入的是否一致（忽略大小写）
       if(StringUtils.isEmpty(code)||!captcha.equalsIgnoreCase(code)){
           return ResBean.error("验证码输入错误，请重新输入！");
       }
        //SpringSecurity自带登录 loadUserByUsername 方法中，通常会根据用户名从数据库或其他数据源中查询用户信息，返回一个实现了 UserDetails 接口的对象
        UserDetails userDetails=userDetailsService.loadUserByUsername(username);
        if(null==userDetails||passwordEncoder.matches(password,userDetails.getPassword())){
            //如果userDetails是空的或者密码匹配失败
            return ResBean.error("用户名或密码不正确");
        }
        if(!userDetails.isEnabled()){
            //账号被禁用
            return ResBean.error("账号被禁用,请联系管理员");
        }
        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //生成token
        String token=jwtTokenUtil.generateToken(userDetails);
        Map<String,String> tokenMap=new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return ResBean.success("登录成功",tokenMap);
    }

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    public Admin getAdminByUserName(String username) {
        //这里使用mybatis-plus 因为我们uap返回Admin，所以泛型要写Admin
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).eq("enabled",true));
    }

    /**
     * 根据用户id查询角色列表
     */
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    /**
     * 获取所有操作员
     * @param keywords
     * @return
     */
    public List<Admin> getAllAdmins(String keywords) {
        return adminMapper.getAllAdmins(AdminUtils.getCurrentAdmin().getId(),keywords);
    }

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    @Transactional
    public ResBean updateAdminRole(Integer adminId, Integer[] rids) {
        //先删除，再根据adminId和rids插入
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        Integer result=adminRoleMapper.addAdminRole(adminId,rids);
        if(rids.length==result){
            return ResBean.success("更新成功！");
        }
        return ResBean.error("更新失败！");
    }

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    public ResBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        //旧密码输入错误是不给更新密码的
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        if(encoder.matches(oldPass,admin.getPassword())){
            //新密码加密之后写入admin
            admin.setPassword(encoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if(result==1){
                return ResBean.success("更新成功！");
            }
        }
        return ResBean.error("更新失败！");
    }


}
