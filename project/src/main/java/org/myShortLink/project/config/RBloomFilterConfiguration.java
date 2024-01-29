package org.myShortLink.project.config;

import org.myShortLink.common.config.RBloomFilterFactory;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RBloomFilterConfiguration {

    @Autowired
    private RBloomFilterFactory rBloomFilterFactory;

    @Autowired
    private RedissonClient redissonClient;

    @Bean
    public RBloomFilter<String> shortUrlCreateBloomFilter() {
        return rBloomFilterFactory.createBloomFilter(redissonClient, "shortUrlCreateBloomFilter");
    }
}
