package com.shiliang.chat.assistant.api.wechat;

import com.shiliang.chat.assistant.core.wechat.WechatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    private final static String FAILURE_XML = "<xml>" +
            "<return_code><![CDATA[FAIL]]></return_code>" +
            "<return_msg><![CDATA[Invalid signature]]></return_msg>" +
            "</xml>";
    public static final String FAILURE_STRING = "Invalid signature";

    private final WechatService wechatService;

    @GetMapping
    public String validateToken(@RequestParam("signature") String signature,
                                @RequestParam("timestamp") String timestamp,
                                @RequestParam("nonce") String nonce,
                                @RequestParam("echostr") String echoString) {
        if (wechatService.isSignatureValid(signature, timestamp, nonce)) {
            return echoString;
        } else {
            return FAILURE_STRING;
        }
    }

    @PostMapping
    public String receiveMessage(@RequestParam("signature") String signature,
                                 @RequestParam("timestamp") String timestamp,
                                 @RequestParam("nonce") String nonce,
                                 @RequestBody String messageBody) {
        if (!wechatService.isSignatureValid(signature, timestamp, nonce)) {
            return FAILURE_XML;
        }
        log.info("receive message: {}", messageBody);
        String reply = wechatService.reply(messageBody);
        log.info("reply message: {}", reply);
        return reply;
    }
}
