package com.example.aneesh.transportapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Archana on 19-10-2017.
 */

public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public void saveUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("Name",user.Name);
        spEditor.putString("UserName",user.Username);
        spEditor.putString("Passsword",user.Password);
        spEditor.putString("VehicleType",user.VehicleType);
        spEditor.putString("LicenseNumber",user.LicenseNumber);
        spEditor.putString("MobileNumber",user.MobileNo);
        spEditor.commit();
    }

    public User getLoggedInUser(){
        String name = userLocalDatabase.getString("Name","");
        String userName = userLocalDatabase.getString("Username","");
//        String password = userLocalDatabase.getString("Password","");
        String vehicleType = userLocalDatabase.getString("VehicleType","");
        String licenseNumber = userLocalDatabase.getString("LicenseNumber","");
        String mobileNo = userLocalDatabase.getString("MobileNo","");
        String referredBy = userLocalDatabase.getString("ReferredBy","");
        return new User(userName,"",licenseNumber,vehicleType,name,mobileNo,referredBy);
    }

    public void setUserLoggedIn(boolean isLoggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn",isLoggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        return userLocalDatabase.getBoolean("LoggedIn",false);
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
