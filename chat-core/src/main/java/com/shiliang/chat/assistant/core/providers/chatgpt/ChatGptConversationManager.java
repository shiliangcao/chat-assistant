package com.shiliang.chat.assistant.core.providers.chatgpt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class ChatGptConversationManager {

    private final ConcurrentMap<String, List<ChatMessage>> chatHistory = Maps.newConcurrentMap();

    public List<ChatMessage> getHistoryMessages(String conversationId) {
        if (StringUtils.isEmpty(conversationId)) {
            return Lists.newArrayList();
        }
        List<ChatMessage> chatMessages = chatHistory.get(conversationId);
        if (CollectionUtils.isEmpty(chatMessages)) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(chatMessages);
    }

    public List<ChatMessage> appendNewMessages(String conversationId, List<ChatMessage> newMessages) {
        return chatHistory.compute(conversationId, (key, existingChatMessages) -> {
            if (existingChatMessages == null) {
                log.info("Messages appended (new): {}, {}", conversationId, newMessages);
                return newMessages;
            }
            ArrayList<ChatMessage> newChatMessages = Lists.newArrayList(existingChatMessages);
            newChatMessages.addAll(newMessages);
            log.info("Messages appended (update): {}, {}", conversationId, newChatMessages);
            return newChatMessages;
        });
    }
}
