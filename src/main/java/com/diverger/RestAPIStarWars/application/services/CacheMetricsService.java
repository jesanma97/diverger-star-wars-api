package com.diverger.RestAPIStarWars.application.services;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheMetricsService {

    private final CacheManager cacheManager;

    public CacheMetricsService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void printCacheStats() {
        Cache cache = cacheManager.getCache("characterCache");
        if (cache != null && cache.getNativeCache() instanceof com.github.benmanes.caffeine.cache.Cache) {
            com.github.benmanes.caffeine.cache.Cache caffeineCache =
                    (com.github.benmanes.caffeine.cache.Cache) cache.getNativeCache();
            CacheStats stats = caffeineCache.stats();
            System.out.println("Cache Stats: " + stats);
        } else {
            System.out.println("Cache not found or not using Caffeine.");
        }
    }
}