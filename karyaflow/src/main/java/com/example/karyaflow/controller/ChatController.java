package com.example.karyaflow.controller;

import com.example.karyaflow.entity.Chat;
import com.example.karyaflow.model.ChatMessage;
import com.example.karyaflow.repo.ChatRepo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("api/messages")
public class ChatController {
    private final ChatRepo repo;

    public ChatController(ChatRepo repo) {
        this.repo = repo;
    }

    @MessageMapping("/chat")
    @SendTo("/chatroom/messages")
    public Chat projectChat(@Valid ChatMessage msg)
    {
        try
        {
            Chat chat = new Chat();
            chat.setContent(msg.getContent());
            chat.setUserName(msg.getUsername());
            chat.setTimestamp(LocalDateTime.now());
            if(msg.getFile()!=null && !msg.getFile().isEmpty()){
                byte[] file= Base64.getDecoder().decode(msg.getFile());
                chat.setFile(file);
            }
            repo.save(chat);
            return chat;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to send the message",e);
        }
    }
    @GetMapping("/listMessages")
    @ResponseStatus(HttpStatus.OK)
    public List<Chat> getChat()
    {
        return repo.findAllByOrderByTimestampAsc();
    }
}
