package com.example.aneesh.transportapp;

/**
 * Created by Archana on 19-10-2017.
 */

public class User {
    String Username,Password,LicenseNumber,VehicleType,Name,MobileNo,ReferredBy;
    public User(String userName, String password, String licenseNumber, String vehicleType,String name,String mobileNo,String referredBy){
        this.Username = userName;
        this.Password = password;
        this.LicenseNumber = licenseNumber;
        this.VehicleType = vehicleType;
        this.Name = name;
        this.MobileNo = mobileNo;
        this.ReferredBy = referredBy;
    }
}
