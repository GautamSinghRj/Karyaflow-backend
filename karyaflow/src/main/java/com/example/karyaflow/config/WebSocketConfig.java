package com.example.karyaflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//Creates a message broker i.e, helps connect between client and websocket server
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //this endpoint is the one that the users will be subscribed to
        config.enableSimpleBroker("/chatroom");
        //this endpoint is the one that is prefixed before every message mapping endpoint
        config.setApplicationDestinationPrefixes("/app");
    }
    //Stomp is protocol that helps in message transfer
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //this endpoint is the one that helps us to connect to websocket
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("http://localhost:5173")
                .withSockJS();
    }

}
