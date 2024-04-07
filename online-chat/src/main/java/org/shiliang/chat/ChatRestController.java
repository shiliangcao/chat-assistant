package org.shiliang.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shiliang.chat.dto.ChatMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRestController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatHistoryManager historyManager;

    @PostMapping("/chat/sendMessage")
    public void sendMessage(@RequestBody ChatMessage chatMessage) {
        log.info("message: {}", chatMessage);
        String destination = Constants.TOPIC_ORDERS + chatMessage.getOrderId();
        messagingTemplate.convertAndSend(destination, chatMessage);
        historyManager.recordMessage(destination, chatMessage);
    }

    @GetMapping("/chat/history/{orderId}")
    public List<ChatMessage> getHistory(@PathVariable("orderId") String orderId) {
        return historyManager.getHistoryMessage(Constants.TOPIC_ORDERS + orderId);
    }
}
