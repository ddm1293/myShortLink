package org.myShortLink.project.service;

import org.myShortLink.project.dto.req.ShortLinkCreateReqDTO;
import org.myShortLink.project.dto.resp.ShortLinkCreateRespDTO;

public interface LinkService {

    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO reqBody);
}
