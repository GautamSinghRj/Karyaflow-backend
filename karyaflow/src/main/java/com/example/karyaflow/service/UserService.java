package com.example.karyaflow.service;

import com.example.karyaflow.entity.User;
import com.example.karyaflow.repo.UserRepo;
import com.example.karyaflow.requestbody.AuthentaticationDetails.RegistrationDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(RegistrationDetails registrationDetails){
        if(userRepo.existsByEmail(registrationDetails.getEmail())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists in the database");
        User user=new User();
        if(registrationDetails.getImage()!=null){
            byte[] image= Base64.getDecoder().decode(registrationDetails.getImage());
            user.setImage(image);
        }
        user.setEmail(registrationDetails.getEmail());
        user.setFullname(registrationDetails.getFullname());
        user.setPassword(passwordEncoder.encode(registrationDetails.getPassword()));
        user.setUsername(registrationDetails.getUsername());
        return userRepo.save(user);
    }
    public Optional<User> findByEmail(String email){
        return userRepo.findByEmail(email);
    }
}
