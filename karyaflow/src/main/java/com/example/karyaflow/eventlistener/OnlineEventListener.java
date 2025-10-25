/*package com.example.karyaflow.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineEventListener {

    private final SimpMessagingTemplate template;//Helps to send message to broker without the message mapping
    private final Set<String> onlineUsers= ConcurrentHashMap.newKeySet();//concurrent hash map is used bcoz so that the website doesn't break when multiple threads try to write or do any other job

    public OnlineEventListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        var attributes=accessor.getSessionAttributes();
        if(attributes!=null){
        String username=(String)attributes.get("username");
        if (username!=null)
        {
        onlineUsers.add(username);
        System.out.println(username+"connected");
        template.convertAndSend("/topic",onlineUsers);
        }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        var attributes=accessor.getSessionAttributes();
        if(attributes!=null) {
            String username=(String) attributes.get("username");
            if (username != null) {
                onlineUsers.remove(username);
                System.out.println(username + "disconnected");
                template.convertAndSend("/topic", onlineUsers);
            }
        }
    }

    public boolean isUserOnline(String username){
        return onlineUsers.contains(username);
    }

    public  Set<String> getOnlineUsers(){
        return onlineUsers;
    }

}
*/
package com.example.karyaflow.eventlistener;

import com.example.karyaflow.entity.User;
import com.example.karyaflow.repo.UserRepo;
import com.example.karyaflow.security.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineEventListener {

    private final SimpMessagingTemplate template;
    private final JwtUtility jwtUtility;
    private final UserRepo userRepo;

    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();
    private final Map<String,String> sessionUser=new ConcurrentHashMap<>();

    @Autowired
    public OnlineEventListener(SimpMessagingTemplate template, JwtUtility jwtUtility, UserRepo userRepo) {
        this.template = template;
        this.jwtUtility = jwtUtility;
        this.userRepo = userRepo;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String token = sha.getFirstNativeHeader("token");
        if (token != null) {
            try {
                String email = jwtUtility.extractEmail(token);
                User user = userRepo.findByEmail(email).orElse(null);
                if (user != null) {
                    String username = user.getUsername();
                    String sessionId=sha.getSessionId();
                    sessionUser.put(sessionId,username);
                    onlineUsers.add(username);
                    System.out.println(username + " connected");
                    template.convertAndSend("/topic", onlineUsers);
                }
            } catch (Exception e) {
                System.out.println("Invalid token during connect: " + e.getMessage());
            }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId=sha.getSessionId();
        String username=sessionUser.remove(sessionId);
        if(username!=null)
        {
            onlineUsers.remove(username);
            System.out.println(username+" disconnected");
            template.convertAndSend("/topic", onlineUsers);
        }
    }
}
