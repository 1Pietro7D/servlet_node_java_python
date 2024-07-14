package com.example.demo.controllers;


import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import com.example.demo.models.Message;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

@Controller
public class WebController {

    private List<Message> messages = new ArrayList<>();

    // Definisce la route per la pagina principale
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("messages", messages);
        return "index";
    }

    // Definisce la route per inviare messaggi
    @PostMapping("/send")
    public String sendMessage(@RequestParam String message, @RequestParam String target /*, Model model*/) {
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

        // Aggiunge il messaggio inviato alla lista dei messaggi
        messages.add(new Message("Java_Server", message));

        // // Aggiunge un messaggio di stato al modello
        // model.addAttribute("status", "Messaggio inviato");
        // model.addAttribute("messages", messages);

        return "redirect:/";
    }

    // Definisce la route per ricevere messaggi
    @PostMapping("/java")
    @ResponseBody
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, String> payload) {
        String from = payload.get("from");
        String message = payload.get("message");

        System.out.println("From: " + from);
        System.out.println("Message: " + message);

        // Aggiunge il messaggio ricevuto alla lista dei messaggi
        messages.add(new Message(from, message));

        return ResponseEntity.ok("Messaggio ricevuto");
    }
}

