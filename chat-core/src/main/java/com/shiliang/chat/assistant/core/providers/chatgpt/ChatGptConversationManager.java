package com.shiliang.chat.assistant.core.providers.chatgpt;

import com.google.common.collect.Lists;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ChatGptConversationManager {

    private static final String CONVERSATION_PREFIX = "conversations";

    private final RMapCache<String, List<ChatMessage>> conversationCache;
    private final Long conversationTTLHours;

    public Map<String, List<ChatMessage>> getActiveConversations() {
        return conversationCache;
    }

    public ChatGptConversationManager(@Value("${conversation.ttl-hours:24}") Long conversationTTLHours,
                                      RedissonClient redissonClient) {
        log.info("Conversation ttl hours: {}", conversationTTLHours);
        this.conversationTTLHours = conversationTTLHours;
        this.conversationCache = redissonClient.getMapCache(CONVERSATION_PREFIX);
    }

    public List<ChatMessage> getHistoryMessages(String conversationId) {
        if (StringUtils.isEmpty(conversationId)) {
            return Lists.newArrayList();
        }
        List<ChatMessage> chatMessages = conversationCache.get(conversationId);
        if (CollectionUtils.isEmpty(chatMessages)) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(chatMessages);
    }

    public List<ChatMessage> appendNewMessages(String conversationId, List<ChatMessage> newMessages) {
        List<ChatMessage> messages = getHistoryMessages(conversationId);
        messages.addAll(newMessages);
        conversationCache.put(conversationId, messages, conversationTTLHours, TimeUnit.HOURS);
        return messages;
    }
}
