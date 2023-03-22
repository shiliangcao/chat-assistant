package com.shiliang.chat.assistant.api.slack.configurations;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Data
@Slf4j
public class SlackConfiguration {

    @Value("${slack.enabled:}")
    private Boolean enabled;

    @Value("${slack.app-token:}")
    private String appToken;

    @Value("${slack.bot-oauth-token:}")
    private String botOauthToken;

    @PostConstruct
    public void init() {
        log.info("Slack configuration: {}", this);

        validate();
    }

    private void validate() {
        if (enabled == Boolean.TRUE) {
            if (StringUtils.isEmpty(appToken) || StringUtils.isEmpty(botOauthToken)) {
                throw new RuntimeException("slack.app-token and slack.bot-oauth-token must be set" +
                        " when slack.enabled is true");
            }
        }
    }

    public boolean isSlackEnabled() {
        if (enabled == null) {
            boolean bothTokenProvided = StringUtils.isNotEmpty(appToken) && StringUtils.isNotEmpty(botOauthToken);

            log.warn("Slack.enabled is not explicitly set, effective value is: {}", bothTokenProvided);
            return bothTokenProvided;
        }
        return enabled;
    }
}
