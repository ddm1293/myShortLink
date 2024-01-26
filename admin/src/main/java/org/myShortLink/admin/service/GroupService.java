package org.myShortLink.admin.service;

import org.myShortLink.admin.dto.req.GroupSortReqDTO;
import org.myShortLink.admin.dto.req.GroupUpdateReqDTO;
import org.myShortLink.admin.dto.resp.GroupRespDTO;

import java.util.List;

public interface GroupService {

    void addGroup(String groupName);

    List<GroupRespDTO> getGroups();

    void updateGroup(GroupUpdateReqDTO reqBody);

    void deleteGroup(String gid);

    void sortGroups(List<GroupSortReqDTO> reqDTO);
}
