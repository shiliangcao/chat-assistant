package com.shiliang.chat.assistant.core;

import com.shiliang.chat.assistant.core.dto.ChatProviderType;
import com.shiliang.chat.assistant.core.dto.ChatRequest;
import com.shiliang.chat.assistant.core.dto.ChatResponse;

public interface ChatProvider {
    ChatProviderType getType();
    ChatResponse process(ChatRequest chatRequest);
}
