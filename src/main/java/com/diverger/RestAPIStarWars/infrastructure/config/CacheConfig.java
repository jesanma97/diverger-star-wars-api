package com.diverger.RestAPIStarWars.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("characterCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES) // Expires after 10 minutes
                .maximumSize(1000) // Maximum size cache
                .recordStats() // Enable stadistics
        );

        cacheManager.setAllowNullValues(true);  // Allow null values if it's necessary
        cacheManager.setAsyncCacheMode(true);
        return cacheManager;
    }
}
