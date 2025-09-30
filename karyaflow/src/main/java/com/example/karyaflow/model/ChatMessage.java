package com.example.karyaflow.model;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ChatMessage {
    @NotBlank(message = "Chat cannot be empty")
    @Size(max = 4000,message = "Chat exceeds the limit value of 4000 characters")
    private String content;
    @Lob
    private String file;
    private String username;
    public String getContent() {
        return content;
    }
    public String getFile() {
        return file;
    }
    public String getUsername() {
        return username;
    }
}
