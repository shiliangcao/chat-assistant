package com.shiliang.chat.assistant.api.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T> {
    private ResponseError error;
    private T data;

    public static <T> Response<T> ok(T data) {
        Response<T> response = new Response();
        response.setData(data);
        return response;
    }

}
