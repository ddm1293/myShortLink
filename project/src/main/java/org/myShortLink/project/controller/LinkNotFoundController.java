package org.myShortLink.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LinkNotFoundController {

    @RequestMapping("/page/notFound")
    public String notFound() {
        return "notFound";
    }
}
