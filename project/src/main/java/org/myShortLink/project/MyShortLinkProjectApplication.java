package org.myShortLink.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.myShortLink.project", "org.myShortLink.common"})
public class MyShortLinkProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyShortLinkProjectApplication.class, args);
    }
}