package org.myShortLink.admin.remote.dto;

import org.myShortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.data.domain.Page;

public interface ShortLinkRemoteService {

    Page<ShortLinkPageRespDTO> getShortLinks(String gid, String orderTag, int currentPage, int size);
}
