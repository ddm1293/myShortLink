package org.myShortLink.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.common.convention.exception.ServiceException;
import org.myShortLink.project.dao.entity.Link;
import org.myShortLink.project.dao.repository.LinkRepository;
import org.myShortLink.project.dto.req.ShortLinkCreateReqDTO;
import org.myShortLink.project.dto.resp.ShortLinkCreateRespDTO;
import org.myShortLink.project.service.LinkService;
import org.myShortLink.project.utils.MurmurHashUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import static org.myShortLink.project.common.constant.LinkGenerateConstant.SUFFIX_GENERATE_CAP;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    private final RBloomFilter<String> shortUrlCreateBloomFilter;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO reqBody) {
        String suffix = generateSuffix(reqBody.getOriginalURL(), reqBody.getDomain());
        String fullShortUrl = reqBody.getDomain() + "/" + suffix;
        Link link = Link.builder()
                .gid(reqBody.getGid())
                .domain(reqBody.getDomain())
                .shortUrl(suffix)
                .fullShortUrl(fullShortUrl)
                .originalUrl(reqBody.getOriginalURL())
                .enabled(true)
                .createdType(reqBody.getCreatedType())
                .validDateType(reqBody.getValidDateType())
                .validDate(reqBody.getValidDate())
                .description(reqBody.getDescription())
                .build();

        try {
            linkRepository.save(link);
        } catch (DuplicateKeyException e) {
            log.warn("Duplicate Full URL Found: ", e);
            shortUrlCreateBloomFilter.add(fullShortUrl);
            // TODO add new error code here
            throw new ServiceException("Duplicate Full URL Found");
        }

        shortUrlCreateBloomFilter.add(fullShortUrl);

        return ShortLinkCreateRespDTO.builder()
                .fullShortURL(link.getFullShortUrl())
                .originalUrl(reqBody.getOriginalURL())
                .gid(reqBody.getGid())
                .build();
    }

    private String generateSuffix(String originalURL, String domain) {
        int generateCount = 0;
        String suffix;
        while (true) {
            if (generateCount > SUFFIX_GENERATE_CAP) {
                // TODO add new error code here
                throw new ServiceException("Too many times generate suffix");
            }
            originalURL = generateCount == 0 ? originalURL : originalURL + System.currentTimeMillis();
            suffix = MurmurHashUtil.convertWithGuava(originalURL);
            if (!shortUrlCreateBloomFilter.contains(domain + "/" + suffix)) {
                break;
            }
            generateCount++;
        }
        return suffix;
    }
}
