package com.example.demo.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.example.demo.handler.WebSocketHandler;

/**
 * Configuration class to enable WebSocket and configure WebSocket handlers.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * Register WebSocket handlers.
     *
     * @param registry the WebSocketHandlerRegistry to register handlers.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Register a WebSocket handler with a specific URL endpoint and allow all origins.
        registry.addHandler(new WebSocketHandler(), "/websocket")
                .setAllowedOrigins("*"); // Allows cross-origin requests from any domain.
    }
}