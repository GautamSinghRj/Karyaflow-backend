package com.example.karyaflow.requestbody.AuthentaticationDetails;

public class RegistrationResponse {
    String token;
    String username;
    String imageUrl;

    public RegistrationResponse(String token, String username, String imageUrl) {
        this.token = token;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
