package com.example.sijili;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("fcmToken")
    private String fcmToken;

    public LoginRequest(String email, String password, String fcmToken) {
        this.email = email;
        this.password = password;
        this.fcmToken = fcmToken;
    }
}
