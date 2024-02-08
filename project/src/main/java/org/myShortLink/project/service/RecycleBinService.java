package org.myShortLink.project.service;

import org.myShortLink.project.dto.req.RecycleBinSaveReqDTO;

public interface RecycleBinService {
    void saveRecycleBin(RecycleBinSaveReqDTO reqBody);
}
