package com.shiliang.chat.assistant.core;

import com.google.common.collect.Maps;
import com.shiliang.chat.assistant.core.common.errorhandling.ChatException;
import com.shiliang.chat.assistant.core.common.errorhandling.ResponseError;
import com.shiliang.chat.assistant.core.common.errorhandling.ResponseErrorCode;
import com.shiliang.chat.assistant.core.dto.ChatProviderType;
import com.shiliang.chat.assistant.core.dto.ChatRequest;
import com.shiliang.chat.assistant.core.dto.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ChatService {

    private final Map<ChatProviderType, ChatProvider> chatProviderMap = Maps.newHashMap();

    public ChatService(List<ChatProvider> chatProviders) {
        chatProviders.forEach(provider -> {
            chatProviderMap.put(provider.getType(), provider);
            log.info("ChatProvider registered, type: {}, provider: {}",
                    provider.getType(), provider.getClass().getName());
        });
    }

    public ChatResponse process(ChatRequest chatRequest) {
        validate(chatRequest);
        normalize(chatRequest);
        return chatProviderMap.get(chatRequest.getChatContext().getChatProviderType()).process(chatRequest);
    }

    private void normalize(ChatRequest chatRequest) {
        if (chatRequest.getChatContext().getChatProviderType() == null) {
            chatRequest.getChatContext().setChatProviderType(ChatProviderType.CHAT_GPT);
        }
    }

    private void validate(ChatRequest chatRequest) {
        if (chatRequest == null || chatRequest.getChatContext() == null) {
            throw ChatException.builder()
                    .responseError(ResponseError.builder()
                            .code(ResponseErrorCode.INVALID_INPUT)
                            .message("chatRequest is null or chatContext in it is null")
                            .build())
                    .build();
        }
    }
}
