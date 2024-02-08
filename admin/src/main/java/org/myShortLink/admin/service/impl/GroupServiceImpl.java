package org.myShortLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.admin.common.context.UserContext;
import org.myShortLink.admin.dao.entity.Group;
import org.myShortLink.admin.dao.repository.GroupRepository;
import org.myShortLink.admin.dto.req.GroupSortReqDTO;
import org.myShortLink.admin.dto.req.GroupUpdateReqDTO;
import org.myShortLink.admin.dto.resp.GroupRespDTO;
import org.myShortLink.admin.remote.service.ShortLinkRemoteService;
import org.myShortLink.admin.remote.dto.resp.GroupCountQueryRespDTO;
import org.myShortLink.admin.service.GroupService;
import org.myShortLink.common.convention.error.BaseErrorCode;
import org.myShortLink.common.convention.exception.ClientException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final ShortLinkRemoteService shortLinkRemoteService;

    @Override
    @Transactional
    public void addGroup(String groupName) {
        addGroup(groupName, UserContext.getUsername());
    }

    @Override
    @Transactional
    public void addGroup(String groupName, String username) {
        log.debug("see username: {}", UserContext.getUsername());
        if (username == null) {
            // TODO new error code
            // TODO need a universal treatment of no header situation
            throw new ClientException("No username in the header potentially");
        }
        groupRepository.save(Group.builder()
                // TODO better gid generation
                .gid(UUID.randomUUID().toString())
                .username(username)
                .groupName(groupName)
                .build());
    }

    @Override
    public List<GroupRespDTO> getGroups() {
        List<Group> groups = groupRepository.getGroups(UserContext.getUsername());
        List<String> gidList = groups.stream().map(Group::getGid).toList();
        Map<String, Integer> countsMap = shortLinkRemoteService.groupCount(gidList).stream()
                .collect(Collectors.toMap(GroupCountQueryRespDTO::getGid, GroupCountQueryRespDTO::getCount));
        return groups.stream().map(group -> {
            GroupRespDTO dto = BeanUtil.toBean(group, GroupRespDTO.class);
            dto.setCount(countsMap.getOrDefault(group.getGid(), 0));
            return dto;
        }).toList();
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
    @Transactional
    public void deleteGroup(String gid) {
        Group group = fetchGroup(UserContext.getUsername(), gid);
        group.setArchived(true);
        groupRepository.save(group);
    }

    @Override
    @Transactional
    public void sortGroups(List<GroupSortReqDTO> reqDTO) {
        String username = UserContext.getUsername();
        reqDTO.forEach(each -> {
            Group group = fetchGroup(username, each.getGid());
            group.setSortOrder(each.getSortOrder());
            groupRepository.save(group);
        });
    }
}
