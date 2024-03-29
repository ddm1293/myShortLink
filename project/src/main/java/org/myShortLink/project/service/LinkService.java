package org.myShortLink.project.service;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.myShortLink.project.dto.req.ShortLinkCreateReqDTO;
import org.myShortLink.project.dto.req.ShortLinkUpdateLinkGroupReqDTO;
import org.myShortLink.project.dto.req.ShortLinkUpdateReqDTO;
import org.myShortLink.project.dto.resp.GroupCountQueryRespDTO;
import org.myShortLink.project.dto.resp.ShortLinkCreateRespDTO;
import org.myShortLink.project.dto.resp.ShortLinkPageRespDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LinkService {

    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO reqBody);

    Page<ShortLinkPageRespDTO> getShortLinksIntoPage(String gid, String orderTag, int currentPage, int size);

    List<GroupCountQueryRespDTO> groupCount(List<String> gidList);

    void updateLink(ShortLinkUpdateReqDTO reqBody);

    void updateLinkGroup(ShortLinkUpdateLinkGroupReqDTO reqBody);

    void restoreUrl(String shortUri, ServletRequest req, ServletResponse resp);
}
