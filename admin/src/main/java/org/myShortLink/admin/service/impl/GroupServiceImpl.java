package org.myShortLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.admin.common.context.UserContext;
import org.myShortLink.admin.common.convention.error.BaseErrorCode;
import org.myShortLink.admin.common.convention.exception.ClientException;
import org.myShortLink.admin.dao.entity.Group;
import org.myShortLink.admin.dao.repository.GroupRepository;
import org.myShortLink.admin.dto.req.GroupUpdateReqDTO;
import org.myShortLink.admin.dto.resp.GroupRespDTO;
import org.myShortLink.admin.service.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public void addGroup(String groupName) {
        Group group = Group.builder()
                .gid(UUID.randomUUID().toString())
                .username(UserContext.getUsername())
                .groupName(groupName)
                .build();
        log.debug("see username: {}", UserContext.getUsername());
        groupRepository.save(group);
    }

    @Override
    public List<GroupRespDTO> getGroups() {
        log.debug("see username: {}", UserContext.getUsername());
        List<Group> list = groupRepository.getGroups(UserContext.getUsername());
        log.debug("see list: {}", list);
        return BeanUtil.copyToList(list, GroupRespDTO.class);
    }

    private Group fetchGroup(String username, String gid) {
        return groupRepository.getGroup(username, gid)
                .orElseThrow(() -> new ClientException(BaseErrorCode.GROUP_NOT_FOUND_ERROR));
    }

    @Override
    @Transactional
    public void updateGroup(GroupUpdateReqDTO reqBody) {
        Group group = fetchGroup(UserContext.getUsername(), reqBody.getGid());
        group.setGroupName(reqBody.getGroupName());
        groupRepository.save(group);
    }

    @Override
    public void deleteGroup(String gid) {
        Group group = fetchGroup(UserContext.getUsername(), gid);
        group.setArchived(true);
        groupRepository.save(group);
    }
}
