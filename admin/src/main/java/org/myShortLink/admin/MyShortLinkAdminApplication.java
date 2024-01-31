package org.myShortLink.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.myShortLink.admin", "org.myShortLink.common"})
public class MyShortLinkAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyShortLinkAdminApplication.class, args);
    }

}
