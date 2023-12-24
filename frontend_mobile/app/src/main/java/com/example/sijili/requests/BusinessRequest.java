package com.example.sijili.requests;

import com.google.gson.annotations.SerializedName;

public class BusinessRequest {

    @SerializedName("companyName")
    private String companyName;
    @SerializedName("address")
    private String address;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("activityType")
    private String activityType;
    @SerializedName("dateOfBirth")
    private String dateOfBirth;
    @SerializedName("nationality")
    private String nationality;
    @SerializedName("nationalityNum")
    private String nationalityNum;
    @SerializedName("isDeclared")
    private boolean isDeclared;

    public BusinessRequest(String companyName, String address, String phoneNumber, String activityType, String dateOfBirth, String nationality, String nationalityNum, boolean isDeclared) {
        this.companyName = companyName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.activityType = activityType;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.nationalityNum = nationalityNum;
        this.isDeclared = isDeclared;
    }
}
