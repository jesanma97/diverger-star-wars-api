package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.controllers;

import com.diverger.RestAPIStarWars.application.services.CacheMetricsService;
import com.diverger.RestAPIStarWars.infrastructure.commons.Endpoints;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.PARENT_URL_CACHE)
public class CacheController {

    private final CacheMetricsService cacheMetricsService;

    public CacheController(CacheMetricsService cacheMetricsService) {
        this.cacheMetricsService = cacheMetricsService;
    }

    @GetMapping(Endpoints.STATS_CACHE)
    public void getCacheStats() {
        cacheMetricsService.printCacheStats();
    }
}
