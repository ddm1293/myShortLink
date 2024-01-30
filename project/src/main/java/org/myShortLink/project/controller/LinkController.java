package org.myShortLink.project.controller;

import lombok.RequiredArgsConstructor;
import org.myShortLink.common.convention.result.Result;
import org.myShortLink.common.convention.result.Results;
import org.myShortLink.project.dto.req.ShortLinkCreateReqDTO;
import org.myShortLink.project.dto.resp.ShortLinkCreateRespDTO;
import org.myShortLink.project.dto.resp.ShortLinkPageRespDTO;
import org.myShortLink.project.service.LinkService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping("/link/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO reqBody) {
        return Results.success(linkService.createShortLink(reqBody));
    }

    @GetMapping("/link/page")
    public Result<Page<ShortLinkPageRespDTO>> getShortLinks(@RequestParam String gid,
                                                            @RequestParam(required = false) String orderTag,
                                                            @RequestParam(defaultValue = "0") int currentPage,
                                                            @RequestParam(defaultValue = "10") int size) {
        return Results.success(linkService.getShortLinks(gid, orderTag, currentPage, size));
    }

}
