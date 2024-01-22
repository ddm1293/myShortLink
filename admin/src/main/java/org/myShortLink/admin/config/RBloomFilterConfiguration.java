package org.myShortLink.admin.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RBloomFilterConfiguration {

    @Bean
    public RBloomFilter<String> usernameBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("usernameBloomFilter");
        cachePenetrationBloomFilter.tryInit(1000000L, 0.0001);
        return cachePenetrationBloomFilter;
    }

    @Bean
    public RBloomFilter<String> userEmailBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("userEmailBloomFilter");
        cachePenetrationBloomFilter.tryInit(1000000L, 0.0001);
        return cachePenetrationBloomFilter;
    }
}
