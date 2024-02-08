package org.myShortLink.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.admin.remote.service.ShortLinkRemoteService;
import org.myShortLink.admin.remote.dto.resp.OriginalLinkInfoRespDTO;
import org.myShortLink.common.convention.result.Result;
import org.myShortLink.common.convention.result.Results;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LinkInfoController {

    private final ShortLinkRemoteService shortLinkRemoteService;

    @GetMapping("/admin/remote/link/getLinkInfo")
    public Result<OriginalLinkInfoRespDTO> getOriginalLinkInfo(@RequestParam("link") String link){
        return Results.success(shortLinkRemoteService.getOriginalLinkInfo(link));
    }
}
