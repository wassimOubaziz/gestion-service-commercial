package com.example.sijili;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LoginResponse {

    @SerializedName("success")
    private boolean success = true;

    public LoginResponse() {
    }

    // You can add more fields based on your server response
    @SerializedName("token")
    private String token;
    @SerializedName("userId")
    private String userId;
    @SerializedName("role")
    private List<String> role;

    @SerializedName("message")
    private String message;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("email")
    private String email;



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

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }
    public String getEmail() {
        return email;
    }
    public String getUserId() {
        return userId;
    }
}