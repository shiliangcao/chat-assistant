package com.shiliang.chat.assistant.api.slack;

import com.shiliang.chat.assistant.api.configurations.SlackConfiguration;
import com.shiliang.chat.assistant.core.ChatService;
import com.shiliang.chat.assistant.core.dto.ChatContext;
import com.shiliang.chat.assistant.core.dto.ChatInput;
import com.shiliang.chat.assistant.core.dto.ChatRequest;
import com.shiliang.chat.assistant.core.dto.ChatResponse;
import com.slack.api.Slack;
import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.AppMentionEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class SlackEventProcessor {
    private final SlackConfiguration slackConfiguration;
    private final ChatService chatService;

    private SocketModeApp socketChatApp;
    private Slack slackInstance;

    @SneakyThrows
    @PostConstruct
    public void init() {

        App chatApp = new App(AppConfig.builder().singleTeamBotToken(slackConfiguration.getAppToken()).build());

        chatApp.event(AppMentionEvent.class, (payload, ctx) -> {
            log.info("App mention event payload: {}, context: {}", payload, ctx);
            CompletableFuture.runAsync(() -> onAppMention(payload, ctx));
            return ctx.ack();
        });

        socketChatApp = new SocketModeApp(slackConfiguration.getAppToken(), chatApp);

        socketChatApp.startAsync();

        slackInstance = Slack.getInstance();
    }

    @SneakyThrows
    @PreDestroy
    public void close() {
        log.info("closing ...");
        if (socketChatApp != null) {
            socketChatApp.close();
        }
        if (slackInstance != null) {
            slackInstance.close();
        }

    }

    // methods to handle events

    @SneakyThrows
    private void onAppMention(EventsApiPayload<AppMentionEvent> eventPayload, EventContext context) {
        AppMentionEvent event = eventPayload.getEvent();
        String slackThreadTs = getSlackMessageThreadId(event);
        ChatResponse chatGptResponse = chatService.process(ChatRequest.builder()
                .chatContext(ChatContext.builder()
                        .conversationId(slackThreadTs)
                        .build())
                .chatInput(ChatInput.builder()
                        .prompt(trimText(event.getText()))
                        .build())
                .build());


        ChatPostMessageRequest chatRequest = ChatPostMessageRequest.builder()
                .channel(event.getChannel())
                .token(slackConfiguration.getBotOauthToken())
                .threadTs(slackThreadTs)
                .text(chatGptResponse.getChatOutput().getRawChatGptResult().getChoices().get(0).getMessage().getContent())
                .build();

        log.info("chat request: {}", chatRequest);
        ChatPostMessageResponse response = slackInstance.methods().chatPostMessage(chatRequest);
        log.info("chat response: {}", response);
    }

    private String getSlackMessageThreadId(AppMentionEvent event) {
        return event.getThreadTs() != null ? event.getThreadTs() : event.getTs();
    }

    private String trimText(String text) {
        int startIndex = text.indexOf("<@");
        while (startIndex != -1) {
            int endIndex = text.indexOf(">", startIndex + 2);
            if (endIndex != -1) {
                text = text.substring(0, startIndex) + text.substring(endIndex + 1);
            } else {
                break;
            }
            startIndex = text.indexOf("<@");
        }
        return text;
    }

}