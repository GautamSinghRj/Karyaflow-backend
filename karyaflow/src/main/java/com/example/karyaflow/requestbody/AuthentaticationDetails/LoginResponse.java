package com.example.karyaflow.requestbody.AuthentaticationDetails;

public class LoginResponse {
    String token;
    String username;
    String image;

    public LoginResponse(String token, String username, String image) {
        this.token = token;
        this.username = username;
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }
}
