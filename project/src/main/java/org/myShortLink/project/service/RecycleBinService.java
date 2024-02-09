package org.myShortLink.project.service;

import org.myShortLink.project.dto.req.RecycleBinSaveReqDTO;
import org.myShortLink.project.dto.resp.ShortLinkPageRespDTO;
import org.springframework.data.domain.Page;

public interface RecycleBinService {
    void saveRecycleBin(RecycleBinSaveReqDTO reqBody);

    Page<ShortLinkPageRespDTO> getDisabledShortLinksIntoPage(String gid, String orderTag, int currentPage, int size);
}
