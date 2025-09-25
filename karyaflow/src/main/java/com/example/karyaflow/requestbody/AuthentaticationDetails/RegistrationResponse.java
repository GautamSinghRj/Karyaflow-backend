package com.example.karyaflow.requestbody.AuthentaticationDetails;

public class RegistrationResponse {
    String token;
    String username;
    public RegistrationResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
