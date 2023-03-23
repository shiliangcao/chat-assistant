package com.shiliang.chat.assistant.core.wechat;

import com.shiliang.chat.assistant.core.wechat.configuration.WechatConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
@Slf4j
public class WechatService {
    private final WechatConfiguration wechatConfiguration;

    public boolean isSignatureValid(String signature, String timestamp, String nonce, String echoString) {
        log.info("Params, signature: {}, timestamp: {}, nonce: {}, echoString: {}",
                signature, timestamp, nonce, echoString);
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
}
