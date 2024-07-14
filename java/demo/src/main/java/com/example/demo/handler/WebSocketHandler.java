package com.example.demo.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket handler to manage text messages sent and received over WebSocket connections.
 */
public class WebSocketHandler extends TextWebSocketHandler {

    /**
     * Called after a new WebSocket connection is established.
     *
     * @param session the WebSocket session.
     * @throws Exception if an error occurs.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Log connection establishment with session ID.
        System.out.println("Connected: " + session.getId());
    }

    /**
     * Handle text messages received from the client.
     *
     * @param session the WebSocket session.
     * @param message the text message received.
     * @throws Exception if an error occurs.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Log the received message payload.
        System.out.println("Received message: " + message.getPayload());

        // Send a response message to the client.
        session.sendMessage(new TextMessage("Hello, " + message.getPayload() + "!"));
    }

    /**
     * Called after a WebSocket connection is closed.
     *
     * @param session the WebSocket session.
     * @param status  the close status.
     * @throws Exception if an error occurs.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Log connection closure with session ID and close status.
        System.out.println("Disconnected: " + session.getId());
    }
}
