package com.example.karyaflow.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ChatMessage {
    @NotBlank(message = "Chat cannot be empty")
    @Size(max = 4000,message = "Chat exceeds the limit value of 4000 characters")
    private String content;

    public String getContent() {
        return content;
    }

}
