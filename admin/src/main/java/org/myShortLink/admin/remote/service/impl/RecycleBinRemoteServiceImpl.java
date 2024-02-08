package org.myShortLink.admin.remote.service.impl;

import org.myShortLink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import org.myShortLink.admin.remote.service.RecycleBinRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RecycleBinRemoteServiceImpl implements RecycleBinRemoteService {

    @Autowired
    private WebClient webClient;

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
}
