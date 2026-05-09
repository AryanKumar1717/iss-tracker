package com.aryan.issTracker.websocket;

import com.aryan.issTracker.service.ISSService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ISSWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ISSService issService;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public ISSWebSocketHandler(ISSService issService) {
        this.issService = issService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Scheduled(fixedRate = 2000)
    public void broadcastPosition() {
        if (sessions.isEmpty()) {
            return;
        }

        issService.getCurrentPosition().subscribe(position -> {
            try {
                String payload = objectMapper.writeValueAsString(position);
                TextMessage message = new TextMessage(payload);

                for (WebSocketSession session : sessions) {
                    if (session.isOpen()) {
                        try {
                            session.sendMessage(message);
                        } catch (IOException e) {
                            // Remove session on send failure
                            sessions.remove(session);
                        }
                    } else {
                        sessions.remove(session);
                    }
                }
            } catch (Exception e) {
                // Ignore serialization or unexpected errors silently for now
            }
        });
    }
}
