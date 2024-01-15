package com.example.sijili.requests;

import com.google.gson.annotations.SerializedName;

public class NotificationsRequest {
    @SerializedName("body")
    private String message;
    @SerializedName("userId")
    private String userId;
    @SerializedName("notificationSeen")
    private boolean notificationSeen;
    @SerializedName("timestamp")
    private String timestamp;

    public boolean isNotificationSeen() {
        return notificationSeen;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    // Constructors, getters, and setters

    public String getNotificationInfo() {
        return message + " - " + timestamp;
    }
}