package com.cloud.mail;

import com.cloud.server.pojo.Employee;
import com.cloud.server.pojo.MailConstants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
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
    @Autowired
    private RedisTemplate redisTemplate;

    //监听mail.welcome这个队列
    @RabbitListener(queues= MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message,Channel channel){//参数有Employee改为了Message message, Channel channel，因为我不仅要获取employee还有获取MsgId等其它信息
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        //消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //获取msgId
        String msgId = (String) headers.get("spring_returned_message_correlation");
        HashOperations hashOperations = redisTemplate.opsForHash();
        try {
            if(hashOperations.entries("mail_log").containsKey(msgId)){
                //判断这个msgId是否在redis中存有了
                LOGGER.info("消息已经被消费了============>{}",msgId);
                /**
                 * 手动确认消息
                 * tag:消息序列
                 * multiple：是否确认多条
                 */
                channel.basicAck(tag,false);
                return;

            }
            MimeMessage msg=javaMailSender.createMimeMessage();//创建消息
            MimeMessageHelper helper = new MimeMessageHelper(msg);
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
            LOGGER.info("邮件发送成功");
            //将消息id存入redis
            hashOperations.put("mail_log",msgId,"OK");
            //手动确认消息
            channel.basicAck(tag,false);
        } catch (Exception e) {
            /**
             * 手动确认消息：
             * tag:消息序号
             * multiple：是否确认多条
             * requeue：是否退货到队列
             */
            try {
                channel.basicAck(tag,false);
            } catch (IOException ex) {
                LOGGER.error("邮件发送失败=====>{}",ex.getMessage());
            }
            LOGGER.error("邮件发送失败=====>{}",e.getMessage());
        }
    }
}
