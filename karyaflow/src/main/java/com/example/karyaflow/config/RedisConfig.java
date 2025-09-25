package com.example.karyaflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
/*
Why it this config is needed?
The RedisTemplate Bean in Spring Boot is a key class that provides a thread-safe interface to interact with a Redis data store.
It abstracts and manages Redis connections and commands, so you can easily read/write data to Redis in your application.
* */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,String> template=new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
