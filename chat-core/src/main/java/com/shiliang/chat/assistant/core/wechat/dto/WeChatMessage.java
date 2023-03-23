package com.shiliang.chat.assistant.core.wechat.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@JacksonXmlRootElement(localName = "xml")
@Data
public class WeChatMessage {

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "FromUserName")
    private String fromUserName;

    @JacksonXmlProperty(localName = "CreateTime")
    private Long createTime;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "MsgType")
    private String msgType;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Content")
    private String content;

    @JacksonXmlProperty(localName = "MsgId")
    private String msgId;

}
