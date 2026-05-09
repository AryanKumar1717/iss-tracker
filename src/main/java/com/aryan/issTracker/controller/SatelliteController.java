package com.aryan.issTracker.controller;

import com.aryan.issTracker.service.SatelliteService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/satellites")
@Validated
public class SatelliteController {

    private final SatelliteService satelliteService;

    public SatelliteController(SatelliteService satelliteService) {
        this.satelliteService = satelliteService;
    }

    @GetMapping("/{noradId}")
    public Mono<JsonNode> getSatellitePosition(@PathVariable @Positive int noradId) {
        return satelliteService.getSatellitePosition(noradId);
    }
}
