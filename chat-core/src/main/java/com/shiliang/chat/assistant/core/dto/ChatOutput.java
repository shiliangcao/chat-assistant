package com.shiliang.chat.assistant.core.dto;

import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatOutput {
    private List<ChatMessage> chatHistory;
    private ChatCompletionResult rawChatGptResult;
}
