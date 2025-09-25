package com.example.karyaflow.service;

import com.example.karyaflow.repo.UserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    //Checks if the user is present in the database
    private UserRepo repo;

    public CustomUserDetailsService(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.example.karyaflow.entity.User user=repo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found in the database"+email));
        return new User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

}
