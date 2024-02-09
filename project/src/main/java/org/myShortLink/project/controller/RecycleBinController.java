package org.myShortLink.project.controller;

import lombok.RequiredArgsConstructor;
import org.myShortLink.common.convention.result.Result;
import org.myShortLink.common.convention.result.Results;
import org.myShortLink.project.dto.req.RecycleBinSaveReqDTO;
import org.myShortLink.project.dto.resp.ShortLinkPageRespDTO;
import org.myShortLink.project.service.RecycleBinService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    @PostMapping("/recycleBin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO reqBody) {
        recycleBinService.saveRecycleBin(reqBody);
        return Results.success();
    }

    @GetMapping("/recycleBin/page")
    public Result<Page<ShortLinkPageRespDTO>> getDisabledShortLinksIntoPage(@RequestParam List<String> gidList,
                                                                            @RequestParam(required = false) String orderTag,
                                                                            @RequestParam(defaultValue = "0") int currentPage,
                                                                            @RequestParam(defaultValue = "10") int size) {
        return Results.success(recycleBinService.getDisabledShortLinksIntoPage(gidList, orderTag, currentPage, size));
    }
}
