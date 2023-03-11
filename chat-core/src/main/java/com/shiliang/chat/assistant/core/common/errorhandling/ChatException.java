package com.shiliang.chat.assistant.core.common.errorhandling;

import lombok.Builder;
import lombok.Getter;

@Builder
public class ChatException extends RuntimeException {
    @Getter
    private ResponseError responseError;
}
