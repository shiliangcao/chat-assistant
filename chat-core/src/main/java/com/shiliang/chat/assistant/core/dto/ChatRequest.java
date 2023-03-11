package com.shiliang.chat.assistant.core.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private ChatContext chatContext;
    private ChatInput chatInput;
}
