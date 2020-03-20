package io.twinterf.notifauth.token;

import org.springframework.http.ResponseEntity;

public class TokenResponse {
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
