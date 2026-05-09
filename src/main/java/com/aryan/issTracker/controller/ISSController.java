package com.aryan.issTracker.controller;

import com.aryan.issTracker.model.ISSPosition;
import com.aryan.issTracker.service.ISSService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/iss")
@Validated
public class ISSController {

    private final ISSService issService;

    public ISSController(ISSService issService) {
        this.issService = issService;
    }

    @GetMapping("/location")
    public Mono<ISSPosition> getLocation() {
        return issService.getCurrentPosition();
    }

    @GetMapping("/passes")
    public Mono<JsonNode> getPasses(
            @RequestParam @Min(-90) @Max(90) double lat,
            @RequestParam @Min(-180) @Max(180) double lon) {
        return issService.getPassTimes(lat, lon);
    }

    @GetMapping("/astronauts")
    public Mono<JsonNode> getAstronauts() {
        return issService.getAstronauts();
    }
}
