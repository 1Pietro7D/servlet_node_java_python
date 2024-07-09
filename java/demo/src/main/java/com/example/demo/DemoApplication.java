package com.example.demo;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("http://localhost:5003/");
	}

}
@Controller
class WebController {

    // Definisce la route per la pagina principale
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Definisce la route per inviare messaggi
    @PostMapping("/send")
    public String sendMessage(@RequestParam String message, @RequestParam String target, Model model) {
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

        // Aggiunge un messaggio di stato al modello
        model.addAttribute("status", "Messaggio inviato");
        return "index";
    }

    // Definisce la route per ricevere messaggi
    @PostMapping("/java")
    @ResponseBody
    // public ResponseEntity<String> receiveMessage(@RequestBody String payload) {
    //     System.out.println("Messaggio ricevuto dal server Java: " + payload);
    //     return ResponseEntity.ok("Messaggio ricevuto");
    // }
	public ResponseEntity<String> receiveMessage(@RequestBody Map<String, String> payload) {
        String from = payload.get("from");
        String message = payload.get("message");

        System.out.println("Messaggio ricevuto dal server Java:");
        System.out.println("From: " + from);
        System.out.println("Message: " + message);

        return ResponseEntity.ok("Messaggio ricevuto");
    }
}