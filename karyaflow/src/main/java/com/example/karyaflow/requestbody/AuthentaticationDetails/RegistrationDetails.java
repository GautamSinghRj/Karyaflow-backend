package com.example.karyaflow.requestbody.AuthentaticationDetails;

import jakarta.persistence.Lob;

public class RegistrationDetails {
    private String email;
    private String username;
    private String fullname;
    private String password;
    @Lob
    private String image;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }
}
