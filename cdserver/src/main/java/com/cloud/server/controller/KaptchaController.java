package com.cloud.server.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码
 */
@RestController
@Slf4j
public class KaptchaController {
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @ApiOperation("验证码")
    @GetMapping(value="/captcha",produces="image/jpeg")//produces保证返回输出的是一个图片
    public void captcha(HttpServletRequest request, HttpServletResponse response){
        // 返回的是一个图形验证码，通过流的方式直接传一个正常的图片过去
        // 那么响应头需要做一些处理，这里处理基本上来说固定的

        // 定义response输出类型为image/jpeg类型
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        //-------------------生成验证码 begin --------------------------
        //获取验证码文本内容
        String text = defaultKaptcha.createText();
        log.info("验证码内容:"+text);
        //将验证码文本内容放入session,后面login的时候可以从session中拿到
        request.getSession().setAttribute("captcha",text);
        //正常生成图形验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream outputStream=null;
        try{
            outputStream= response.getOutputStream();
            //输出流输出图片，格式为jpg
            ImageIO.write(image,"jpg",outputStream);
            outputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(null!=outputStream)
            {
                try{
                    //关闭流
                    outputStream.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        //-------------------生成验证码 end --------------------------
    }
}
