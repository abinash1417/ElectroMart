package com.electromart.backend.service;

import com.electromart.backend.model.Product;
import com.electromart.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public AiService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String chat(String userMessage) {

        // Fetch products and limit context
        List<Product> products = productRepository.findAll();
        StringBuilder productContext = new StringBuilder();
        for (Product p : products) {
            productContext.append("- ")
                    .append(p.getName())
                    .append(", Category: ").append(p.getCategory() != null ? p.getCategory().getName() : "N/A")
                    .append(", Price: Rs.").append(p.getPrice())
                    .append(", Stock: ").append(p.getStock())
                    .append("\n");
        }

        String systemPrompt = "You are ElectroBot, a friendly AI assistant for ElectroMart electronics store. "
                + "Help customers find products, compare items, guide on orders, cart, shipping and returns. "
                + "Current products:\n" + productContext
                + "Be concise, friendly and helpful. Mention product names and prices when recommending.";

        // Build request
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemPrompt);

        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.3-70b-versatile");
        body.put("messages", List.of(systemMsg, userMsg));
        body.put("max_tokens", 400);
        body.put("temperature", 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.groq.com/openai/v1/chat/completions",
                    request,
                    Map.class
            );

            List<Map> choices = (List<Map>) response.getBody().get("choices");
            Map message = (Map) choices.get(0).get("message");
            return (String) message.get("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public String compareProducts(List<Map<String, Object>> products) {
        StringBuilder productInfo = new StringBuilder();
        for (Map<String, Object> p : products) {
            productInfo.append("Product: ").append(p.get("name"))
                    .append(" | Category: ").append(p.get("category") != null ? ((Map)p.get("category")).get("name") : "N/A")
                    .append(" | Price: Rs.").append(p.get("price"))
                    .append(" | Stock: ").append(p.get("stock"))
                    .append(" | Description: ").append(p.get("description"))
                    .append("\n");
        }

        String prompt = "You are ElectroBot, an expert electronics advisor at ElectroMart.\n\n"
                + "Compare these products for the customer:\n\n"
                + productInfo
                + "\nProvide:\n"
                + "1. Key differences between the products\n"
                + "2. Which product is best for different types of users\n"
                + "3. Your final recommendation with clear reasoning\n"
                + "Be concise, friendly and helpful. Use bullet points where needed.";

        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "You are ElectroBot, a smart electronics advisor. Give clear, helpful product comparisons.");

        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.3-70b-versatile");
        body.put("messages", List.of(systemMsg, userMsg));
        body.put("max_tokens", 600);
        body.put("temperature", 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.groq.com/openai/v1/chat/completions",
                    request,
                    Map.class
            );
            List<Map> choices = (List<Map>) response.getBody().get("choices");
            Map message = (Map) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, unable to compare products right now. Please try again!";
        }
    }
}