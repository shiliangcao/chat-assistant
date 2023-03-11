package com.shiliang.chat.assistant.core.providers;

import com.google.common.collect.Lists;
import com.shiliang.chat.assistant.core.ChatProvider;
import com.shiliang.chat.assistant.core.dto.ChatOutput;
import com.shiliang.chat.assistant.core.dto.ChatProviderType;
import com.shiliang.chat.assistant.core.dto.ChatRequest;
import com.shiliang.chat.assistant.core.dto.ChatResponse;
import com.shiliang.chat.assistant.core.providers.chatgpt.OpenAiConfiguration;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class ChatGptProvider implements ChatProvider {

    private final OpenAiConfiguration openAiConfiguration;
    private final OpenAiService openAiService;

    public ChatGptProvider(OpenAiConfiguration openAiConfiguration) {
        this.openAiConfiguration = openAiConfiguration;
        this.openAiService = new OpenAiService(OpenAiService
                .buildApi(openAiConfiguration.getApiKey(), Duration.ofSeconds(10)));
    }

    @Override
    public ChatProviderType getType() {
        return ChatProviderType.CHAT_GPT;
    }

    @Override
    public ChatResponse process(ChatRequest chatRequest) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(openAiConfiguration.getChatDefaultModel())
                .messages(buildMessages(chatRequest))
                .maxTokens(openAiConfiguration.getChatDefaultMaxTokens())
                .build();

        ChatCompletionResult chatCompletionResult = openAiService.createChatCompletion(chatCompletionRequest);

        return ChatResponse.builder()
                .chatOutput(ChatOutput.builder()
                        .chatCompletionResult(chatCompletionResult)
                        .build())
                .build();
    }

    private List<ChatMessage> buildMessages(ChatRequest chatRequest) {
        return Lists.newArrayList(new ChatMessage("user", chatRequest.getChatInput().getPrompt()));
    }
}
