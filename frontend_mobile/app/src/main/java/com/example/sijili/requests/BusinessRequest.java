package com.example.sijili.requests;

import com.google.gson.annotations.SerializedName;

public class BusinessRequest {
    @SerializedName("_id")
    private String id;
    @SerializedName("userId")
    private String userId;
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
    @SerializedName("name")
    private String name;
    @SerializedName("status")
    private String status;
    @SerializedName("paid")
    private boolean paid;

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setNationalityNum(String nationalityNum) {
        this.nationalityNum = nationalityNum;
    }

    public void setDeclared(boolean declared) {
        isDeclared = declared;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }


    public String getId() {
        return id;
    }
    public String getUserId() {
        return userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getNationalityNum() {
        return nationalityNum;
    }

    public boolean isDeclared() {
        return isDeclared;
    }


    public boolean isPaid() {
        return paid;
    }
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

    public BusinessRequest() {
    }
}
