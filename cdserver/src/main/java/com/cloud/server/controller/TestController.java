package com.cloud.server.controller;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/system/ai")
public class TestController {

    public static final RestTemplate restTemplate = new RestTemplate();

    @Value("${deepseek.api.token}")
    private String apiToken;

    @Value("${deepseek.api.url}")
    private String apiUrl;


    @ApiOperation(value="调用ds")
    @PostMapping("/deepSeek")
    public String callDeepSeek(@RequestBody String question) {

        // 创建消息列表
        List<Map<String, Object>> messages = new ArrayList<>();

        // 添加系统消息
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant");
        messages.add(systemMessage);

        // 添加用户消息
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", question);
        messages.add(userMessage);

        // 创建请求 Map
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("model", "deepseek-chat");
        requestMap.put("messages", messages);
        requestMap.put("frequency_penalty", 0);
        requestMap.put("max_tokens", 2048);
        requestMap.put("presence_penalty", 0);
        requestMap.put("response_format", Collections.singletonMap("type", "text"));
        requestMap.put("stop", null);
        requestMap.put("stream", false);
        requestMap.put("stream_options", null);
        requestMap.put("temperature", 1);
        requestMap.put("top_p", 1);
        requestMap.put("tools", null);
        requestMap.put("tool_choice", "none");
        requestMap.put("logprobs", false);
        requestMap.put("top_logprobs", null);

        // 将 requestMap 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(requestMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
        // 解析响应 JSON
        JSONObject jsonResponse = JSONObject.parseObject(response.getBody());
        String generatedText = jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
        return generatedText;
    }

    @ApiOperation(value="获取账户余额")
    @GetMapping("/getBalance")
    public String getBalance() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);


        // 发送 GET 请求
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.deepseek.com/user/balance",
                HttpMethod.GET,
                entity,
                String.class
        );
        System.out.println(response.getBody());
        return response.getBody();
    }


}
