package com.himiko.server.config;

import com.himiko.server.handler.PackageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public static Logger logger = LoggerFactory.getLogger(RawWebSocketConfig.class);
    public PackageHandler packageHandler;

    public RawWebSocketConfig()
    {
        this.packageHandler = new PackageHandler();
    }

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
                logger.info("WebSocket verbunden... Session ID:"  + session.getId());
            }

            @Override
            public void handleMessage(WebSocketSession session, org.springframework.web.socket.WebSocketMessage<?> message) throws Exception {
                logger.info("Nachricht erhalten: " + message.getPayload());
                packageHandler.handlePackage(message.getPayload().toString(), session);
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                logger.error("❌ Transport-Error: " + exception.getMessage());
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                logger.info("🔒 Verbindung geschlossen: " + closeStatus);
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };
    }
}