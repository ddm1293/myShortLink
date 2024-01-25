package org.myShortLink.admin.service;

import org.myShortLink.admin.dto.resp.GroupRespDTO;

import java.util.List;

public interface GroupService {

    void addGroup(String groupName);

    List<GroupRespDTO> getGroups();
}
