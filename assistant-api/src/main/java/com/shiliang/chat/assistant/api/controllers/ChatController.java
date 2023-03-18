package com.shiliang.chat.assistant.api.controllers;

import com.shiliang.chat.assistant.core.common.Response;
import com.shiliang.chat.assistant.core.ChatService;
import com.shiliang.chat.assistant.core.dto.ChatRequest;
import com.shiliang.chat.assistant.core.dto.ChatResponse;
import com.shiliang.chat.assistant.core.providers.chatgpt.ChatGptConversationManager;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final ChatGptConversationManager conversationManager;

    @GetMapping("/admin/conversations")
    public Response<Map<String, List<ChatMessage>>> getActiveConversations() {
        return Response.ok(conversationManager.getActiveConversations());
    }

    @PostMapping
    public Response<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
        log.info("ChatRequest: {}", chatRequest);
        return Response.ok(chatService.process(chatRequest));
    }
}
