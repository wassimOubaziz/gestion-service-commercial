package com.example.sijili.requests;

import com.google.gson.annotations.SerializedName;

public class MessageRequest {

    @SerializedName("_id")
    private String id;
    @SerializedName("message")
    private String message;
    @SerializedName("timeElapsed")
    private String timestamp;
    @SerializedName("userId")
    private String userId;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("email")
    private String email;




    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }
}
