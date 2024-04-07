package org.shiliang.chat;

import lombok.RequiredArgsConstructor;
import org.shiliang.chat.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        // 根据订单ID动态构建目的地路径
        String destination = "/topic/orders/" + chatMessage.getOrderId();

        // 使用messagingTemplate广播消息
        messagingTemplate.convertAndSend(destination, chatMessage);
    }
}