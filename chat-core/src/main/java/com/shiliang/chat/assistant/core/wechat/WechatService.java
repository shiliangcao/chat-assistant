package com.shiliang.chat.assistant.core.wechat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.shiliang.chat.assistant.core.wechat.configuration.WechatConfiguration;
import com.shiliang.chat.assistant.core.wechat.dto.WeChatMessage;
import com.shiliang.chat.assistant.core.wechat.dto.WeChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
@Slf4j
public class WechatService {
    private final WechatConfiguration wechatConfiguration;

    public boolean isSignatureValid(String signature, String timestamp, String nonce) {
        log.info("Params, signature: {}, timestamp: {}, nonce: {}", signature, timestamp, nonce);

        String[] arr = {wechatConfiguration.getToken(), timestamp, nonce};
        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            sb.append(s);
        }

        String hash = DigestUtils.sha1Hex(sb.toString());
        log.info("hash: {}", hash);

        return hash.equals(signature);
    }

    @SneakyThrows
    public String reply(String messageBody) {

        ObjectMapper objectMapper = new XmlMapper();
        WeChatMessage weChatMessage = objectMapper.readValue(messageBody, WeChatMessage.class);

        WeChatResponse weChatResponse = new WeChatResponse();
        weChatResponse.setToUserName(weChatMessage.getFromUserName());
        weChatResponse.setFromUserName(weChatMessage.getToUserName());
        weChatResponse.setCreateTime(System.currentTimeMillis());
        weChatResponse.setMsgType("text");
        weChatResponse.setContent(weChatMessage.getContent());

        return objectMapper.writeValueAsString(weChatResponse);
    }

}
