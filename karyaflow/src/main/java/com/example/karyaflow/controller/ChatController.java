package com.example.karyaflow.controller;

import com.example.karyaflow.entity.Chat;
import com.example.karyaflow.model.ChatMessage;
import com.example.karyaflow.repo.ChatRepo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;

@Controller
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
            Authentication auth= SecurityContextHolder.getContext().getAuthentication();
            String userName=auth !=null? auth.getName() : "Unknown user";

            Chat chat = new Chat();
            chat.setContent(msg.getContent());
            chat.setUserName(userName);
            chat.setTimestamp(LocalDateTime.now());
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
