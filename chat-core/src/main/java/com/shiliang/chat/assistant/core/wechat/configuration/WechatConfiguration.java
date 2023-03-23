package com.shiliang.chat.assistant.core.wechat.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
@Data
public class WechatConfiguration {

    @Value("${wechat.token:}")
    private String token;

    @PostConstruct
    public void init() {
        log.info("Wechat Configuration: {}", this);
    }
}
