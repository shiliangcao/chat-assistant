package com.shiliang.chat.assistant.core.dto;

import com.theokanning.openai.completion.chat.ChatCompletionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatOutput {
    private ChatCompletionResult chatCompletionResult;
}
