package com.himiko.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class RawWebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(myWebSocketHandler(), "/ws")       // <-- Endpoint-Pfad "/ws"
                .setAllowedOriginPatterns("*");                // erlaubt CORS für alle Origins
    }

    public WebSocketHandler myWebSocketHandler() {
        return new WebSocketHandler() {

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                System.out.println("✅ WebSocket verbunden... Session ID:" + session.getId());
            }

            @Override
            public void handleMessage(WebSocketSession session, org.springframework.web.socket.WebSocketMessage<?> message) throws Exception {
                System.out.println("⬅️ Nachricht erhalten: " + message.getPayload());
                // Echo-Beispiel:
                session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                System.err.println("❌ Transport-Error: " + exception.getMessage());
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                System.out.println("🔒 Verbindung geschlossen: " + closeStatus);
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };
    }
}