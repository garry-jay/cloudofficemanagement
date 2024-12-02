package com.cloud.server.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 消息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain=true)
public class ChatMsg {
    private String from;//谁发的消息
    private String to;//发到哪里去
    private String content;//内容
    private LocalDateTime date;//日期
    private String formNickName;//昵称
}
