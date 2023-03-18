package com.shiliang.chat.assistant.core.providers.chatgpt;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ChatGptConversationManager {

    private final Cache<String, List<ChatMessage>> conversationCache;

    public Map<String, List<ChatMessage>> getActiveConversations() {
        return conversationCache.asMap();
    }

    public ChatGptConversationManager(@Value("${conversation.ttl-hours:24}") Long conversationTTLHours) {
        log.info("Conversation ttl hours: {}", conversationTTLHours);
        conversationCache = CacheBuilder.newBuilder()
                .expireAfterWrite(conversationTTLHours, TimeUnit.HOURS)
                .concurrencyLevel(8)
                .build();
    }

    public List<ChatMessage> getHistoryMessages(String conversationId) {
        if (StringUtils.isEmpty(conversationId)) {
            return Lists.newArrayList();
        }
        List<ChatMessage> chatMessages = conversationCache.getIfPresent(conversationId);
        if (CollectionUtils.isEmpty(chatMessages)) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(chatMessages);
    }

    public List<ChatMessage> appendNewMessages(String conversationId, List<ChatMessage> newMessages) {
        return conversationCache.asMap().compute(conversationId, (key, existingChatMessages) -> {
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
