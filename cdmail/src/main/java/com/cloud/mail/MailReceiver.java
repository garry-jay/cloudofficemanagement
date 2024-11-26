package com.cloud.mail;

import com.cloud.server.pojo.Employee;
import com.cloud.server.pojo.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;


/**
 * 消息接收者
 */
@Component
public class MailReceiver {
    private static  final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);//日志

    @Autowired
    private JavaMailSender javaMailSender;//邮件发送bean
    @Autowired
    private MailProperties mailProperties;//邮件配置
    @Autowired
    private TemplateEngine templateEngine;//邮件引擎

    //监听mail.welcome这个队列
    @RabbitListener(queues= MailConstants.MAIL_QUEUE_NAME)
    public void handler(Employee employee){
        MimeMessage msg=javaMailSender.createMimeMessage();//创建消息
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            //发件人
            helper.setFrom(mailProperties.getUsername());
            //收件人
            helper.setTo(employee.getEmail());
            //邮件主题
            helper.setSubject("入职欢迎邮件");
            //邮件发送日期
            helper.setSentDate(new Date());
            //邮件内容
            Context context = new Context();
            context.setVariable("name",employee.getName());
            context.setVariable("posName",employee.getPosition().getName());
            context.setVariable("joblevelName",employee.getJoblevel().getName());
            context.setVariable("departmentName",employee.getDepartment().getName());
            //mail对应模板引擎mail.html
            String mail = templateEngine.process("mail", context);
            helper.setText(mail,true);
            //发送邮件
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            LOGGER.error("邮件发送失败=====>{}",e.getMessage());
        }
    }
}
