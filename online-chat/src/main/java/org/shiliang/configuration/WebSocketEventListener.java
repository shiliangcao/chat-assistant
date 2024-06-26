package org.shiliang.configuration;

import lombok.RequiredArgsConstructor;
import org.shiliang.chat.ChatHistoryManager;
import org.shiliang.chat.Constants;
import org.shiliang.chat.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatHistoryManager historyManager;

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String destination = headers.getDestination();

        // 检查目的地是否是我们想要监听的topic
        assert destination != null;
        if (destination.startsWith(Constants.TOPIC_ORDERS)) {
            // 假设identity作为自定义header传入
            String identity = headers.getFirstNativeHeader("identity");
            if (identity != null) {
                // 发送加入通知
                ChatMessage systemEvent = ChatMessage.builder()
                        .orderId(destination.substring(Constants.TOPIC_ORDERS.length()))
                        .from("system")
                        .text(identity + " has joined the conversation!")
                        .build();
                messagingTemplate.convertAndSend(destination, systemEvent);
                historyManager.recordMessage(destination, systemEvent);
            }
        }
    }
}

