package org.myShortLink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.myShortLink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import org.myShortLink.admin.remote.dto.resp.GroupCountQueryRespDTO;
import org.myShortLink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import org.myShortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.myShortLink.admin.remote.service.ShortLinkRemoteService;
import org.myShortLink.common.convention.result.Result;
import org.myShortLink.common.convention.result.Results;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkRemoteService shortLinkRemoteService;

    @GetMapping("/admin/remote/link/page")
    public Result<Page<ShortLinkPageRespDTO>> getShortLinks(@RequestParam String gid,
                                                            @RequestParam(required = false) String orderTag,
                                                            @RequestParam(defaultValue = "0") int currentPage,
                                                            @RequestParam(defaultValue = "10") int size) {
        return Results.success(shortLinkRemoteService.getShortLinks(gid, orderTag, currentPage, size));
    }

    @GetMapping("/admin/remote/link/countInGroup")
    public Result<List<GroupCountQueryRespDTO>> groupCount(@RequestParam("gidList") List<String> gidList) {
        return Results.success(shortLinkRemoteService.groupCount(gidList));
    }

    @PostMapping("/admin/remote/link/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO reqBody) {
        return Results.success(shortLinkRemoteService.createShortLink(reqBody));
    }
}
