package com.diverger.RestAPIStarWars.application.services;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheMetricsService {

    private final CacheManager cacheManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheMetricsService.class);

    public CacheMetricsService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void printCacheStats() {
        Cache cache = cacheManager.getCache("characterCache");
        if (cache != null && cache.getNativeCache() instanceof com.github.benmanes.caffeine.cache.Cache) {
            com.github.benmanes.caffeine.cache.Cache caffeineCache =
                    (com.github.benmanes.caffeine.cache.Cache) cache.getNativeCache();
            CacheStats stats = caffeineCache.stats();
            LOGGER.info("Cache Stats: {}", stats);
        } else {
            LOGGER.info("Cache not found or not using Caffeine.");
        }
    }
}