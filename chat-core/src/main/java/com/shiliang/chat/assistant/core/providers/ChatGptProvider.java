package com.shiliang.chat.assistant.core.providers;

import com.google.common.collect.Lists;
import com.shiliang.chat.assistant.core.ChatProvider;
import com.shiliang.chat.assistant.core.dto.ChatOutput;
import com.shiliang.chat.assistant.core.dto.ChatProviderType;
import com.shiliang.chat.assistant.core.dto.ChatRequest;
import com.shiliang.chat.assistant.core.dto.ChatResponse;
import com.shiliang.chat.assistant.core.providers.chatgpt.ChatGptConversationManager;
import com.shiliang.chat.assistant.core.providers.chatgpt.OpenAiConfiguration;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class ChatGptProvider implements ChatProvider {

    private final OpenAiConfiguration openAiConfiguration;
    private final OpenAiService openAiService;
    private final ChatGptConversationManager conversationManager;

    @Value("${openai.chat.name:john}")
    private String name;

    @PostConstruct
    public void init() {
        log.info("Name: {}", name);
    }

    public ChatGptProvider(OpenAiConfiguration openAiConfiguration, ChatGptConversationManager conversationManager) {
        this.openAiConfiguration = openAiConfiguration;
        this.openAiService = new OpenAiService(openAiConfiguration.getApiKey(),
                Duration.ofSeconds(this.openAiConfiguration.getTimeoutSeconds()));
        this.conversationManager = conversationManager;
    }

    @Override
    public ChatProviderType getType() {
        return ChatProviderType.CHAT_GPT;
    }

    @Override
    public ChatResponse process(ChatRequest chatRequest) {
        String conversationId = getOrGenerateConversationId(chatRequest);
        List<ChatMessage> messages = conversationManager.getHistoryMessages(conversationId);
        boolean initRequest = CollectionUtils.isEmpty(messages);
        if (initRequest) {
            messages.add(introMessage());
        }
        ChatMessage newChatMessage =
                new ChatMessage(ChatMessageRole.USER.value(), chatRequest.getChatInput().getPrompt());
        messages.add(newChatMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(openAiConfiguration.getChatDefaultModel())
                .messages(messages)
                .maxTokens(openAiConfiguration.getChatDefaultMaxTokens())
                .build();

        ChatCompletionResult chatCompletionResult = openAiService.createChatCompletion(chatCompletionRequest);

        List<ChatMessage> latestChatHistory = null;
        if (isSuccess(chatCompletionResult)) {
            List<ChatMessage> newMessages = Lists.newArrayList();
            if (initRequest) {
                newMessages.add(introMessage());
            }
            newMessages.add(newChatMessage);
            newMessages.add(chatCompletionResult.getChoices().get(0).getMessage());
            latestChatHistory = conversationManager.appendNewMessages(conversationId, newMessages);
            chatRequest.getChatContext().setConversationId(conversationId);
        }

        return ChatResponse.builder()
                .chatOutput(ChatOutput.builder()
                        .rawChatGptResult(chatCompletionResult)
                        .chatHistory(latestChatHistory)
                        .build())
                .chatContext(chatRequest.getChatContext())
                .build();
    }

    private ChatMessage introMessage() {
        return new ChatMessage(ChatMessageRole.USER.value(),
                "Your name is " + name + ", use this name in this conversation");
    }

    private static boolean isSuccess(ChatCompletionResult chatCompletionResult) {
        return StringUtils.equals(chatCompletionResult.getChoices().get(0).getFinishReason(), "stop");
    }

    private static String getOrGenerateConversationId(ChatRequest chatRequest) {
        String conversationId = chatRequest.getChatContext().getConversationId();
        return StringUtils.isEmpty(conversationId) ? UUID.randomUUID().toString() : conversationId;
    }

}
