package com.electromart.backend.dto;

public class AiChatDTO {
    private String message;

    public AiChatDTO() {}

    public AiChatDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}