package com.example.demo.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

//@Component
public class SessionsSocket {
    
    // ArrayList per memorizzare le sessioni WebSocket
    private List<WebSocketSession> sessions;

    // Istanza Singleton
    private static SessionsSocket instance;

    // Costruttore privato per impedire l'instanziamento
    private SessionsSocket() {
        sessions = new ArrayList<>();
    }

    // Metodo pubblico per ottenere l'istanza Singleton
    public static synchronized SessionsSocket getInstance() {
        if (instance == null) {
            instance = new SessionsSocket();
        }
        return instance;
    }
    
    // Metodo per aggiungere una sessione WebSocket
    public /*synchronized?*/ void addSession(WebSocketSession session) {
        sessions.add(session);
    }
    public /*synchronized?*/ void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    // Metodo per ottenere tutte le sessioni WebSocket
    public /*synchronized?*/ List<WebSocketSession> getSessions() {
        return new ArrayList<>(sessions);
    }

    //
    public /*synchronized?*/ void sendMessageToAllSessions(TextMessage textMessage) throws IOException{
        for (WebSocketSession session : this.getSessions()) {
            //if (session.isOpen()) {
                session.sendMessage(textMessage);
            //}        
        }
    }
}
