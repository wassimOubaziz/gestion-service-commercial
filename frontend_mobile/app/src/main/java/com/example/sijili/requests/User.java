package com.example.sijili.requests;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("first_name")
    private String firstname;
    @SerializedName("last_name")
    private String lastname;
    @SerializedName("email")
    private String email;

    // Getters and setters...

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}