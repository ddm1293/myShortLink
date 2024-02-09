package org.myShortLink.admin.remote.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import org.myShortLink.admin.remote.dto.req.RecycleBinRemoveReqDTO;
import org.myShortLink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import org.myShortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.myShortLink.admin.remote.service.RecycleBinRemoteService;
import org.myShortLink.admin.service.GroupService;
import org.myShortLink.common.convention.exception.ServiceException;
import org.myShortLink.common.convention.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecycleBinRemoteServiceImpl implements RecycleBinRemoteService {

    @Autowired
    private WebClient webClient;

    private final GroupService groupService;

    @Override
    public void saveRecycleBin(RecycleBinSaveReqDTO reqBody) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/recycleBin/save")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(reqBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public Page<ShortLinkPageRespDTO> getDisabledShortLinksIntoPage(String orderTag, int currentPage, int size) {
        List<String> gidList = groupService.getUserGroupGids();

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recycleBin/page")
                        .queryParam("gidList", gidList)
                        .queryParam("currentPage", currentPage)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            return new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .registerModule(new PageJacksonModule())
                    .registerModule(new SortJacksonModule())
                    .readValue(response, new TypeReference<Result<Page<ShortLinkPageRespDTO>>>() {})
                    .getData();
        } catch (JsonProcessingException e) {
            log.error("see JsonProcessingException", e);
            // TODO new error code
            throw new ServiceException("Error when deserializing Json");
        }
    }

    @Override
    public void recoverFromRecycleBin(RecycleBinRecoverReqDTO reqBody) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/recycleBin/recover")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(reqBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public void removeFromRecycleBin(RecycleBinRemoveReqDTO reqBody) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/recycleBin/remove")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(reqBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
