package com.electromart.backend.controller;

import com.electromart.backend.dto.ContactMessageDTO;
import com.electromart.backend.model.ContactMessage;
import com.electromart.backend.service.ContactService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/send")
    public ContactMessage sendMessage(@RequestBody ContactMessageDTO dto) {
        return contactService.saveMessage(
                dto.getName(),
                dto.getEmail(),
                dto.getSubject(),
                dto.getMessage()
        );
    }
}