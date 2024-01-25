package org.myShortLink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.myShortLink.admin.common.convention.result.Result;
import org.myShortLink.admin.common.convention.result.Results;
import org.myShortLink.admin.dto.req.GroupAddReqDTO;
import org.myShortLink.admin.dto.req.GroupUpdateReqDTO;
import org.myShortLink.admin.dto.resp.GroupRespDTO;
import org.myShortLink.admin.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/hello")
    public Result<String> hello() {
        return Results.success("hello world");
    }

    @PostMapping("/group")
    public Result<Void> addGroup(@RequestBody GroupAddReqDTO reqBody) {
        groupService.addGroup(reqBody.getGroupName());
        return Results.success();
    }

    @GetMapping("/group")
    public Result<List<GroupRespDTO>> getGroups() {
        return Results.success(groupService.getGroups());
    }

    @PutMapping("/group")
    public Result<Void> updateGroup(@RequestBody GroupUpdateReqDTO reqBody) {
        groupService.updateGroup(reqBody);
        return Results.success();
    }

    @DeleteMapping("/group")
    public Result<Void> deleteGroup(@RequestParam String gid) {
        groupService.deleteGroup(gid);
        return Results.success();
    }
}
