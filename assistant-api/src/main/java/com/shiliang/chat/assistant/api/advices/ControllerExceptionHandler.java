package com.shiliang.chat.assistant.api.advices;

import com.shiliang.chat.assistant.core.common.errorhandling.ChatException;
import com.shiliang.chat.assistant.core.common.Response;
import com.shiliang.chat.assistant.core.common.errorhandling.ResponseError;
import com.shiliang.chat.assistant.core.common.errorhandling.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response<?>> handleRuntimeException(Exception e) {
        log.error("Runtime exception", e);
        return ResponseEntity.ok(Response.builder()
                .error(ResponseError.builder()
                        .code(ResponseErrorCode.UNKNOWN_RUNTIME_EXCEPTION)
                        .message(e.getMessage())
                        .build())
                .build());
    }

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<Response<?>> handleChatException(ChatException e) {
        log.error("Chat exception", e);
        return ResponseEntity.ok(Response.builder().error(e.getResponseError()).build());
    }
}
