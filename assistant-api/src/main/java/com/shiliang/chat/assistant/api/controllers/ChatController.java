package com.shiliang.chat.assistant.api.controllers;

import com.shiliang.chat.assistant.api.controllers.dto.Response;
import com.shiliang.chat.assistant.core.ChatService;
import com.shiliang.chat.assistant.core.dto.ChatRequest;
import com.shiliang.chat.assistant.core.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;
    @PostMapping
    public Response<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
        return Response.ok(chatService.process(chatRequest));
    }
}
