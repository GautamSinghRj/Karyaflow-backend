package com.example.karyaflow.service;

import com.example.karyaflow.entity.User;
import com.example.karyaflow.repo.UserRepo;
import com.example.karyaflow.requestbody.AuthentaticationDetails.RegistrationDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadService service;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, FileUploadService service) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.service = service;
    }

    public User createUser(RegistrationDetails registrationDetails) throws IOException {
        if(userRepo.existsByEmail(registrationDetails.getEmail())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists in the database");
        User user=new User();
        if(registrationDetails.getImage()!=null && !registrationDetails.getImage().trim().isEmpty()){
            byte[] image= Base64.getDecoder().decode(registrationDetails.getImage());
            String url=service.uploadFile(image);
            user.setImage(url);
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

    public List<User> getUsers(){
        return userRepo.findAll();
    }

    public List<String> getUsernames(){return userRepo.findAllUsernames();}
}
