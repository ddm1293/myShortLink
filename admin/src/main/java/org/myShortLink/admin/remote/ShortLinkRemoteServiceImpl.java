package org.myShortLink.admin.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.admin.remote.dto.ShortLinkRemoteService;
import org.myShortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.myShortLink.common.convention.exception.ServiceException;
import org.myShortLink.common.convention.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class ShortLinkRemoteServiceImpl implements ShortLinkRemoteService {

    @Autowired
    private WebClient webClient;

    public Page<ShortLinkPageRespDTO> getShortLinks(String gid, String orderTag, int currentPage, int size) {
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/link/page")
                        .queryParam("gid", gid)
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
}