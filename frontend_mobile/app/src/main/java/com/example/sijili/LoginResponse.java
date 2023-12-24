package com.example.sijili;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LoginResponse {

    @SerializedName("success")
    private boolean success = true;

    // You can add more fields based on your server response
    @SerializedName("token")
    private String token;

    @SerializedName("role")
    private List<String> role;

    @SerializedName("message")
    private String message;

    public String getToken() {
        return token;
    }
    public boolean isSuccess() {
        return success;
    }
    public List<String> getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }
}