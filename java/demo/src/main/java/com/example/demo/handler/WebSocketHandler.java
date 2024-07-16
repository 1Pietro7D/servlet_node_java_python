package com.example.demo.handler;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


/**
 * WebSocket handler to manage text messages sent and received over WebSocket connections.
 */

public class WebSocketHandler extends TextWebSocketHandler {
    private SessionsSocket sessionsSocket = SessionsSocket.getInstance();
    //  @Autowired
    //      SessionsSocket sessionsSocket;
    /**
     * Called after a new WebSocket connection is established.
     *
     * @param session the WebSocket session.
     * @throws Exception if an error occurs.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionsSocket.addSession(session);
    }

    /**
     * Handle text messages received from the client.
     *
     * @param session the WebSocket session.
     * @param message the text message received.
     * @throws Exception if an error occurs.
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("Ciao al momento questo canale è fuori uso, cerca di mandare il messaggio attraverso una richesta http post su /send o /java"));
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
        sessionsSocket.removeSession(session);
    }
}
