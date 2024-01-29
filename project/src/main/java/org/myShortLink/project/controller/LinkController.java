package org.myShortLink.project.controller;

import lombok.RequiredArgsConstructor;
import org.myShortLink.common.convention.result.Result;
import org.myShortLink.common.convention.result.Results;
import org.myShortLink.project.dto.req.ShortLinkCreateReqDTO;
import org.myShortLink.project.dto.resp.ShortLinkCreateRespDTO;
import org.myShortLink.project.service.LinkService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping("/link/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO reqBody) {
        return Results.success(linkService.createShortLink(reqBody));
    }

}
