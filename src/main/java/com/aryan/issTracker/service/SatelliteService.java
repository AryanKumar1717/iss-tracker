package com.aryan.issTracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SatelliteService {

    private final WebClient webClient;

    @Value("${n2yo.api.key}")
    private String apiKey;

    public SatelliteService(WebClient webClient) {
        this.webClient = webClient;
    }

    // Caches the response for this specific satellite.
    // Note: To enforce a strict 30-second TTL, a cache provider like Caffeine or Redis is required.
    @Cacheable("satellite-positions")
    public Mono<String> getSatellitePosition(int noradId) {
        String url = String.format("https://api.n2yo.com/rest/v1/satellite/positions/%d/28.6/77.2/0/2/&apiKey=%s", noradId, apiKey);
        
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }
}
