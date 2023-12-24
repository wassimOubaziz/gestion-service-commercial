package com.example.sijili.requests;

import com.google.gson.annotations.SerializedName;

public class OTPRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("verificationCode")
    private String verificationCode;

    public OTPRequest(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;

    }
}
