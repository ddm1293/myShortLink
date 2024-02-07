package org.myShortLink.project.service;

import org.myShortLink.project.dto.resp.OriginalLinkInfoRespDTO;

public interface LinkInfoService {

    OriginalLinkInfoRespDTO getOriginalLinkInfo(String link);
}
