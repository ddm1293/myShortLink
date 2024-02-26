package org.myShortLink.admin.remote.service;

import org.myShortLink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import org.myShortLink.admin.remote.dto.resp.GroupCountQueryRespDTO;
import org.myShortLink.admin.remote.dto.resp.OriginalLinkInfoRespDTO;
import org.myShortLink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import org.myShortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShortLinkRemoteService {

    Page<ShortLinkPageRespDTO> getShortLinks(String gid, String orderTag, int currentPage, int size);

    List<GroupCountQueryRespDTO> groupCount(List<String> gidList);

    OriginalLinkInfoRespDTO getOriginalLinkInfo(String link);

    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO reqBody);
}
