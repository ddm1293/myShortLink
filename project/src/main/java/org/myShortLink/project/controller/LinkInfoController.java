package org.myShortLink.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.common.convention.result.Result;
import org.myShortLink.common.convention.result.Results;
import org.myShortLink.project.dto.resp.OriginalLinkInfoRespDTO;
import org.myShortLink.project.service.LinkInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LinkInfoController {

    private final LinkInfoService linkInfoService;

    @GetMapping("/link/getLinkInfo")
    public Result<OriginalLinkInfoRespDTO> getOriginalLinkInfo(@RequestParam("link") String link){
        return Results.success(linkInfoService.getOriginalLinkInfo(link));
    }
}
