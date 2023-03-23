package com.shiliang.chat.assistant.api.wechat;

import com.shiliang.chat.assistant.core.wechat.WechatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/wechat")
public class WechatController {

    private final WechatService wechatService;

    @PostMapping("/validate-token")
    public String processInputMessage(@RequestParam("signature") String signature,
                                      @RequestParam("timestamp") String timestamp,
                                      @RequestParam("nonce") String nonce,
                                      @RequestParam("echostr") String echoString) {
        return wechatService.validateToken(signature, timestamp, nonce, echoString);
    }
}
