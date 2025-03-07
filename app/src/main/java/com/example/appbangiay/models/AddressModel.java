package com.example.appbangiay.models;
//Model cho địa chỉ
public class AddressModel {
    private String fullName;
    private String phoneNumber;
    private String fullAddressInfo;
    private Boolean selected;
    public AddressModel(String fullName, String phoneNumber, String fullAddressInfo, Boolean selected) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.fullAddressInfo = fullAddressInfo;
        this.selected = selected;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullAddressInfo() {
        return fullAddressInfo;
    }

    public void setFullAddressInfo(String fullAddressInfo) {
        this.fullAddressInfo = fullAddressInfo;
    }
}
