package com.electromart.backend.controller;

import com.electromart.backend.dto.AiChatDTO;
import com.electromart.backend.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody AiChatDTO aiChatDTO) {
        String response = aiService.chat(aiChatDTO.getMessage());
        return ResponseEntity.ok(Map.of("response", response));
    }

    @PostMapping("/compare")
    public ResponseEntity<?> compare(@RequestBody List<Map<String, Object>> products) {
        String response = aiService.compareProducts(products);
        return ResponseEntity.ok(Map.of("response", response));
    }
}