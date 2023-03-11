package com.shiliang.chat.assistant.core.providers.chatgpt;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Data
@Slf4j
public class OpenAiConfiguration {
    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.chat.default-model}")
    private String chatDefaultModel;

    @Value("${openai.chat.default-max-tokens}")
    private Integer chatDefaultMaxTokens;

    @PostConstruct
    public void init() {
        log.info("OpenAiConfiguration: {}", this);
    }
}
