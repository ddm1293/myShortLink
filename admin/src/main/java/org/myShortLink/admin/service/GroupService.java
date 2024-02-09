package org.myShortLink.admin.service;

import org.myShortLink.admin.dto.req.GroupSortReqDTO;
import org.myShortLink.admin.dto.req.GroupUpdateReqDTO;
import org.myShortLink.admin.dto.resp.GroupRespDTO;

import java.util.List;

public interface GroupService {

    void addGroup(String groupName);

    void addGroup(String groupName, String username);

    List<GroupRespDTO> getGroups();

    void updateGroup(GroupUpdateReqDTO reqBody);

    void deleteGroup(String gid);

    void sortGroups(List<GroupSortReqDTO> reqDTO);

    List<String> getUserGroupGids();
}
