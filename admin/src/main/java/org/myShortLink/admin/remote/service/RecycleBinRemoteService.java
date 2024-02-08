package org.myShortLink.admin.remote.service;

import org.myShortLink.admin.remote.dto.req.RecycleBinSaveReqDTO;

public interface RecycleBinRemoteService {

    void saveRecycleBin(RecycleBinSaveReqDTO reqBody);
}
