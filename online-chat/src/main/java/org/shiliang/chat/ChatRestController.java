package org.shiliang.chat;

import lombok.RequiredArgsConstructor;
import org.shiliang.chat.dto.ChatMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRestController {
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/chat/sendMessage")
    public void sendMessage(@RequestBody ChatMessage chatMessage) {
        messagingTemplate.convertAndSend("/topic/orders/" + chatMessage.getOrderId(), chatMessage);
    }
}
