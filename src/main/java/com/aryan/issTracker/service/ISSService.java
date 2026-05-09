package com.aryan.issTracker.service;

import com.aryan.issTracker.model.ISSPosition;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ISSService {

    private final WebClient webClient;

    public ISSService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ISSPosition> getCurrentPosition() {
        return webClient.get()
            .uri("http://api.open-notify.org/iss-now.json")
            .retrieve()
            .bodyToMono(JsonNode.class)
            .map(jsonNode -> {
                ISSPosition position = new ISSPosition();
                position.setMessage(jsonNode.path("message").asText());
                position.setTimestamp(jsonNode.path("timestamp").asText());
                
                JsonNode issPositionNode = jsonNode.path("iss_position");
                if (!issPositionNode.isMissingNode()) {
                    position.setLatitude(issPositionNode.path("latitude").asDouble());
                    position.setLongitude(issPositionNode.path("longitude").asDouble());
                }
                
                return position;
            });
    }

    public Mono<JsonNode> getPassTimes(double lat, double lon) {
        return webClient.get()
            .uri("http://api.open-notify.org/iss-pass.json?lat={lat}&lon={lon}", lat, lon)
            .retrieve()
            .bodyToMono(JsonNode.class);
    }

    public Mono<JsonNode> getAstronauts() {
        return webClient.get()
            .uri("http://api.open-notify.org/astros.json")
            .retrieve()
            .bodyToMono(JsonNode.class);
    }
}
