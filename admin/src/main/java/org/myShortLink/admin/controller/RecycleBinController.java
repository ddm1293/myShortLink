package org.myShortLink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.myShortLink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import org.myShortLink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import org.myShortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.myShortLink.admin.remote.service.RecycleBinRemoteService;
import org.myShortLink.common.convention.result.Result;
import org.myShortLink.common.convention.result.Results;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinRemoteService recycleBinRemoteServiceService;

    @PostMapping("/admin/remote/recycleBin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO reqBody) {
        recycleBinRemoteServiceService.saveRecycleBin(reqBody);
        return Results.success();
    }

    @GetMapping("/admin/remote/recycleBin/page")
    public Result<Page<ShortLinkPageRespDTO>> getDisabledShortLinksIntoPage(@RequestParam(required = false) String orderTag,
                                                                            @RequestParam(defaultValue = "0") int currentPage,
                                                                            @RequestParam(defaultValue = "10") int size) {
        return Results.success(recycleBinRemoteServiceService.getDisabledShortLinksIntoPage(orderTag, currentPage, size));
    }

    @PostMapping("/admin/remote/recycleBin/recover")
    public Result<Void> recoverFromRecycleBin(@RequestBody RecycleBinRecoverReqDTO reqBody) {
        recycleBinRemoteServiceService.recoverFromRecycleBin(reqBody);
        return Results.success();
    }
}
