package org.myShortLink.project.service;

import org.myShortLink.project.dto.req.RecycleBinRecoverReqDTO;
import org.myShortLink.project.dto.req.RecycleBinRemoveReqDTO;
import org.myShortLink.project.dto.req.RecycleBinSaveReqDTO;
import org.myShortLink.project.dto.resp.ShortLinkPageRespDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RecycleBinService {
    void saveRecycleBin(RecycleBinSaveReqDTO reqBody);

    Page<ShortLinkPageRespDTO> getDisabledShortLinksIntoPage(List<String> gidList, String orderTag, int currentPage, int size);

    void recoverFromRecycleBin(RecycleBinRecoverReqDTO reqBody);

    void removeInRecycleBin(RecycleBinRemoveReqDTO reqBody);
}
