package org.myShortLink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.myShortLink.admin.remote.dto.ShortLinkRemoteService;
import org.myShortLink.admin.remote.dto.resp.GroupCountQueryRespDTO;
import org.myShortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.myShortLink.common.convention.result.Result;
import org.myShortLink.common.convention.result.Results;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
