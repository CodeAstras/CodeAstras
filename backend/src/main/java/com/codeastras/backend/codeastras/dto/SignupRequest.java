package com.codeastras.backend.codeastras.dto;

public class SignupRequest {
    private String email;
    private String password;

    // No-arg constructor required for Jackson
    public SignupRequest() {}

    public SignupRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
