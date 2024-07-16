package com.example.demo.controllers;


import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.TextMessage;
import com.example.demo.handler.SessionsSocket;
import com.example.demo.models.Message;

@Controller
public class WebController {
    //@Autowired
    //private SessionsSocket sessionsSocket;
    
    private SessionsSocket sessionsSocket = SessionsSocket.getInstance();
    private List<Message> messages = new ArrayList<>();

    // Definisce la route per la pagina principale
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("messages", messages);
        return "index";
    }

    // Definisce la route per inviare messaggi
    @PostMapping("/send")
    public String sendMessage(@RequestParam String message, @RequestParam String target /*, Model model*/) throws IOException {
        // Definisce gli URL di destinazione per i diversi target
        String[] urls = {
            "http://localhost:5001/node",
            "http://localhost:5002/python"
        };

        // Crea un'istanza di RestTemplate per inviare le richieste HTTP
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>("{\"from\": \"Java_Server\", \"message\":\"" + message + "\"}", headers);

        // Invio del messaggio ai server specificati
        if (target.equals("all")) {
            for (String url : urls) {
                restTemplate.postForEntity(url, entity, String.class);
            }
        } else if (target.equals("node")) {
            restTemplate.postForEntity(urls[0], entity, String.class);
        } else if (target.equals("python")) {
            restTemplate.postForEntity(urls[1], entity, String.class);
        }
        
        Message newMessage = new Message("Java_Server", message);

        // Aggiunge il messaggio inviato alla lista dei messaggi
        messages.add(newMessage);
        
        sessionsSocket.sendMessageToAllSessions(new TextMessage(newMessage.toString()));
        // Ottenere tutte le sessioni WebSocket
        // List<WebSocketSession> allSessions = sessionsSocket.getSessions();
        // for (WebSocketSession session : sessionsSocket.getSessions()) {
        //     // System.out.println(session.getId());
        //     //TextMessage newTextMessage = new TextMessage("nuovo testo");
        //     session.sendMessage(new TextMessage(newMessage.toString()));
        // }
        
            return "redirect:/";
    }

    // define the route to receive the message
    @PostMapping("/java")
    @ResponseBody
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, String> payload) throws IOException {
        String from = payload.get("from");
        String message = payload.get("message");
        System.out.println("payload: " + payload);
        // System.out.println("From: " + from);
        // System.out.println("Message: " + message);

        // Add received message to the list
        Message newMessage = new Message(from, message);
        messages.add(newMessage);
        sessionsSocket.sendMessageToAllSessions(new TextMessage(newMessage.toString()));
        return ResponseEntity.ok("Messaggio ricevuto");
    }
}

