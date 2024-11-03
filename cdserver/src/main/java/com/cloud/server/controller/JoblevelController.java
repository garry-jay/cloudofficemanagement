package com.cloud.server.controller;


import com.cloud.server.pojo.Joblevel;
import com.cloud.server.pojo.ResBean;
import com.cloud.server.service.IJoblevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
@RestController
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {
    @Autowired
    private IJoblevelService joblevelService;

    @ApiOperation(value="获取所有职称")
    @GetMapping("/")
    public List<Joblevel> getAllJobLevel(){
        return joblevelService.list();
    }


    @ApiOperation(value="添加职称")
    @PostMapping("/")
    public ResBean addJobLevel(@RequestBody Joblevel joblevel){
        joblevel.setCreateDate(LocalDateTime.now());
        if(joblevelService.save(joblevel)){
            return ResBean.success("添加成功！");
        }
        else{
            return ResBean.error("添加失败！");
        }
    }

    @ApiOperation(value="更新职称")
    @PutMapping("/")
    public ResBean updateJobLevel(@RequestBody Joblevel joblevel){
        if(joblevelService.updateById(joblevel)){
            return ResBean.success("更新成功！");
        }
        else{
            return ResBean.error("更新失败！");
        }
    }

    @ApiOperation(value="删除职称")
    @DeleteMapping("/{id}")
    public ResBean deleteJobLevel(@PathVariable Integer id){
        if(joblevelService.removeById(id)){
            return ResBean.success("删除成功！");
        }else{
            return ResBean.error("删除失败！");
        }
    }

    @ApiOperation(value="批量删除")
    @DeleteMapping("/")
    public ResBean deleteJobLevelByIds(Integer[] ids){
        if(joblevelService.removeByIds(Arrays.asList(ids))){
            return ResBean.success("删除成功！");
        }else{
            return ResBean.error("删除失败！");
        }
    }
}
