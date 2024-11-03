package com.cloud.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResBean {
    private long code;//返回状态码
    private String message;
    private Object obj;

    //成功返回
    public static ResBean success(String message){
        return new ResBean(200,message,null);
    }

    //成功返回
    public static ResBean success(String message,Object obj){
        return new ResBean(200,message,obj);
    }

    /**
     * 失败返回结果
     * @param message
     * @return
     */
    public static ResBean error(String message){
        return new ResBean(500,message,null);
    }

    /**
     * 失败返回结果
     * @param message
     * @param obj
     * @return
     */
    public static ResBean error(String message,Object obj){
        return new ResBean(500,message,obj);
    }
}
