package org.myShortLink.project.controller;

import lombok.RequiredArgsConstructor;
import org.myShortLink.project.service.LinkService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;
}
