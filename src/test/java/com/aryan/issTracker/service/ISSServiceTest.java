package com.aryan.issTracker.service;

import com.aryan.issTracker.model.ISSPosition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ISSServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ISSService issService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void getCurrentPosition_ReturnsCorrectLatLon() throws Exception {
        String jsonResponse = "{\"message\": \"success\", \"timestamp\": 1620560000, \"iss_position\": {\"latitude\": \"28.6\", \"longitude\": \"77.2\"}}";
        JsonNode mockNode = objectMapper.readTree(jsonResponse);

        // Mock fluent WebClient chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://api.open-notify.org/iss-now.json")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(mockNode));

        Mono<ISSPosition> result = issService.getCurrentPosition();

        StepVerifier.create(result)
                .expectNextMatches(position -> 
                        position.getLatitude() == 28.6 && 
                        position.getLongitude() == 77.2 &&
                        "success".equals(position.getMessage()))
                .verifyComplete();
    }

    @Test
    void getCurrentPosition_HandlesEmptyResponseGracefully() throws Exception {
        // iss_position object is missing here
        String jsonResponse = "{\"message\": \"success\", \"timestamp\": 1620560000}";
        JsonNode mockNode = objectMapper.readTree(jsonResponse);

        // Mock fluent WebClient chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://api.open-notify.org/iss-now.json")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(mockNode));

        Mono<ISSPosition> result = issService.getCurrentPosition();

        StepVerifier.create(result)
                .expectNextMatches(position -> 
                        position.getLatitude() == 0.0 && 
                        position.getLongitude() == 0.0 &&
                        "success".equals(position.getMessage()))
                .verifyComplete();
    }

    @Test
    void getPassTimes_CallsCorrectUrl() throws Exception {
        double lat = 28.6;
        double lon = 77.2;
        String jsonResponse = "{\"message\": \"success\"}";
        JsonNode mockNode = objectMapper.readTree(jsonResponse);

        // Mock fluent WebClient chain for getPassTimes
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("http://api.open-notify.org/iss-pass.json?lat={lat}&lon={lon}"), eq(lat), eq(lon)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(mockNode));

        Mono<JsonNode> result = issService.getPassTimes(lat, lon);

        StepVerifier.create(result)
                .expectNextMatches(node -> "success".equals(node.path("message").asText()))
                .verifyComplete();

        // Verify that the correct URL with params was called
        verify(requestHeadersUriSpec).uri("http://api.open-notify.org/iss-pass.json?lat={lat}&lon={lon}", lat, lon);
    }
}
