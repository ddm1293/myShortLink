package org.myShortLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.admin.dao.entity.Group;
import org.myShortLink.admin.dao.repository.GroupRepository;
import org.myShortLink.admin.dto.resp.GroupRespDTO;
import org.myShortLink.admin.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Override
    public void addGroup(String groupName) {
        Group group = Group.builder()
                .gid(UUID.randomUUID().toString())
                .groupName(groupName)
                .build();
        log.debug("see group: {}", group);
        groupRepository.save(group);
    }

    @Override
    public List<GroupRespDTO> getGroups() {
        // TODO get username
        List<Group> list = groupRepository.getGroups(null);
        return BeanUtil.copyToList(list, GroupRespDTO.class);
    }
}
