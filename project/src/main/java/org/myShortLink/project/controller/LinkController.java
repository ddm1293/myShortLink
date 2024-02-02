package org.myShortLink.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.common.convention.result.Result;
import org.myShortLink.common.convention.result.Results;
import org.myShortLink.project.dto.req.ShortLinkCreateReqDTO;
import org.myShortLink.project.dto.req.ShortLinkUpdateLinkGroupReqDTO;
import org.myShortLink.project.dto.req.ShortLinkUpdateReqDTO;
import org.myShortLink.project.dto.resp.GroupCountQueryRespDTO;
import org.myShortLink.project.dto.resp.ShortLinkCreateRespDTO;
import org.myShortLink.project.dto.resp.ShortLinkPageRespDTO;
import org.myShortLink.project.service.LinkService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    @GetMapping("/link/countInGroup")
    public Result<List<GroupCountQueryRespDTO>> groupCount(@RequestParam("gidList") List<String> gidList) {
        return Results.success(linkService.groupCount(gidList));
    }

    @PutMapping("/link/update")
    public Result<Void> updateLink(@RequestBody ShortLinkUpdateReqDTO reqBody) {
        linkService.updateLink(reqBody);
        return Results.success();
    }

    @PutMapping("/link/updateGroup")
    public Result<Void> updateLinkGroup(@RequestBody ShortLinkUpdateLinkGroupReqDTO reqBody) {
        linkService.updateLinkGroup(reqBody);
        return Results.success();
    }

}
