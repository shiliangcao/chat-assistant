package com.shiliang.chat.assistant.api.configurations;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Data
@Slf4j
public class SlackConfiguration {

    @Value("${slack.app-token}")
    private String appToken;

    @Value("${slack.bot-oauth-token}")
    private String botOauthToken;

    @PostConstruct
    public void init() {
        log.info("Slack configuration: {}", this);
    }
}
