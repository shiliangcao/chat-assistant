package com.shiliang.chat.assistant.api.controllers;

import com.shiliang.chat.assistant.core.common.Response;
import com.shiliang.chat.assistant.core.ChatService;
import com.shiliang.chat.assistant.core.dto.ChatRequest;
import com.shiliang.chat.assistant.core.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    @PostMapping
    public Response<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
        log.info("ChatRequest: {}", chatRequest);
        return Response.ok(chatService.process(chatRequest));
    }
}
