package com.aryan.issTracker.config;

import com.aryan.issTracker.websocket.ISSWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ISSWebSocketHandler issWebSocketHandler;

    public WebSocketConfig(ISSWebSocketHandler issWebSocketHandler) {
        this.issWebSocketHandler = issWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(issWebSocketHandler, "/ws/iss").setAllowedOrigins("*");
    }
}
