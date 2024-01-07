package com.example.sijili.requests;

public class PasswordResetRequest {

    private String email;
    private String newPassword;

    public PasswordResetRequest(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }

    // Getters and setters (or use Lombok for simplicity)
}
