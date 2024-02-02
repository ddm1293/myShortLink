package org.myShortLink.admin.remote.dto;

import org.myShortLink.admin.remote.dto.resp.GroupCountQueryRespDTO;
import org.myShortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShortLinkRemoteService {

    Page<ShortLinkPageRespDTO> getShortLinks(String gid, String orderTag, int currentPage, int size);

    List<GroupCountQueryRespDTO> groupCount(List<String> gidList);
}
