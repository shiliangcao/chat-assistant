package com.shiliang.chat.assistant.api;

import com.shiliang.chat.assistant.core.CoreModuleBeacon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {
        ApiModuleBeacon.class,
        CoreModuleBeacon.class
})
public class ChatSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatSpringApplication.class);
    }
}
