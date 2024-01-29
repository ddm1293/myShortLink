package org.myShortLink.common.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class RBloomFilterFactory {

    public RBloomFilter<String> createBloomFilter(RedissonClient redissonClient, String name) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(name);
        bloomFilter.tryInit(1000000L, 0.0001);
        return bloomFilter;
    }
}
