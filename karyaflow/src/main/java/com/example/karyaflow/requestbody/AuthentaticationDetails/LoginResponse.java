package com.example.karyaflow.requestbody.AuthentaticationDetails;

public class LoginResponse {
    String token;
    String username;

    public LoginResponse(String token, String username) {
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
