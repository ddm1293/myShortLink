package org.myShortLink.admin.remote.service;

import org.myShortLink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import org.myShortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.data.domain.Page;

public interface RecycleBinRemoteService {

    void saveRecycleBin(RecycleBinSaveReqDTO reqBody);

    Page<ShortLinkPageRespDTO> getDisabledShortLinksIntoPage(String orderTag, int currentPage, int size);
}
