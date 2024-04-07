package org.shiliang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {
        OnlineChatModuleBeacon.class
})
public class OnlineChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineChatApplication.class);
    }
}