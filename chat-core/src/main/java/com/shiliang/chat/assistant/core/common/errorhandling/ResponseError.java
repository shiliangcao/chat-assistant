package com.shiliang.chat.assistant.core.common.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {
    private ResponseErrorCode code;
    private String message;
}
