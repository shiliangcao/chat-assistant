package org.shiliang.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shiliang.chat.dto.ChatMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRestController {
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/chat/sendMessage")
    public void sendMessage(@RequestBody ChatMessage chatMessage) {
        log.info("message: {}", chatMessage);
        messagingTemplate.convertAndSend("/topic/orders/" + chatMessage.getOrderId(), chatMessage);
    }
}
