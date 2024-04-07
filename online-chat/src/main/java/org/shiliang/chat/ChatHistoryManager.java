package org.shiliang.chat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.shiliang.chat.dto.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@Service
public class ChatHistoryManager {
    ConcurrentMap<String, List<ChatMessage>> chatHistory = Maps.newConcurrentMap();

    public void recordMessage(String topic, ChatMessage message) {
        chatHistory.compute(topic, (key, value) -> {
            if (value == null) {
                value = new ArrayList<>();
            }
            value.add(message);
            return value;
        });
    }

    public List<ChatMessage> getHistoryMessage(String topic) {
        return chatHistory.get(topic);
    }

    public void clear(String topic) {
        chatHistory.remove(topic);
    }
}
