package com.example.aneesh.transportapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Pair;
import android.widget.Toast;

import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Archana on 21-10-2017.
 */

public class DALWrapper {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://192.168.1.3:80/api/TransportApp/";
    private final Activity activity;
    public DALWrapper(Context context,Activity activity){
        progressDialog = new ProgressDialog(context);
        this.activity = activity;
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
    }

    public void GetUserData(Activity activity,User user, ServerCallback callback){
        progressDialog.show();
        new GetUserDataAsAsync(user,callback).execute();
    }

    public void SaveUserData(User user, ServerCallback callback){
        progressDialog.show();
        new SaveUserDataAsAsync(user,callback).execute();
    }

    public class SaveUserDataAsAsync extends AsyncTask<Void,Void,Void>{
        User user;
        ServerCallback callback;
        public SaveUserDataAsAsync(User user, ServerCallback callback){
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            //Encoded String - we will have to encode string by our custom method (Very easy)
            String encodedStr = "";//= getEncodedData(dataToSend);
            try {

                JSONObject jsonObj = new JSONObject();
                jsonObj.put("Name", user.Name);
                jsonObj.put("UserName", user.Username);
                jsonObj.put("Password", user.Password);
                jsonObj.put("ReferredBy", user.ReferredBy);
                jsonObj.put("VehicleType", user.VehicleType);
                jsonObj.put("MobileNumber", user.MobileNo);
                jsonObj.put("LicenseNumber", user.LicenseNumber);
                encodedStr = jsonObj.toString();
            }
            catch (JSONException e){

            }

            //Will be used if we want to read some data from server
            BufferedReader reader = null;

            //Connection Handling
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "RegisterUserDetails");
                //Opening the connection (Not setting or using CONNECTION_TIMEOUT)
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                //Post Method
                con.setRequestMethod("POST");
                con.setRequestProperty( "Content-Type", "application/json; charset=UTF-8" );
                con.setRequestProperty( "Content-Length", String.valueOf(encodedStr.length()));
                //To enable inputting values using POST method
                //(Basically, after this we can write the dataToSend to the body of POST method)
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                //Writing dataToSend to outputstreamwriter
                writer.write(encodedStr);
                //Sending the data to the server - This much is enough to send data to server
                //But to read the response of the server, you will have to implement the procedure below
                writer.flush();

                //Data Read Procedure - Basically reading the data comming line by line
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while((line = reader.readLine()) != null) { //Read till there is something available
                    sb.append(line + "\n");     //Reading and saving line by line - not all at once
                }
                line = sb.toString();           //Saving complete data received in string, you can do it differently
                if(line == "Success"){
                    Toast.makeText(activity.getBaseContext(), "Registration Successful. Please login ", Toast.LENGTH_LONG).show();
                    activity.startActivity(new Intent(activity,LoginActivity.class));
                }
                else{
                    Toast.makeText(activity.getBaseContext(), "Registration Failure. Please try again", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();     //Closing the
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Same return null, but if you want to return the read string (stored in line)
            //then change the parameters of AsyncTask and return that type, by converting
            //the string - to say JSON or user in your case
            return null;
        }

        private String getEncodedData(ArrayList<Pair<String,String>> data) {
            StringBuilder sb = new StringBuilder();
            for(Pair<String,String> key : data) {
                String value = null;
                try {
                    value = URLEncoder.encode(key.second,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(sb.length()>0)
                    sb.append("&");

                sb.append(key.first + "=" + value);
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callback.Done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class GetUserDataAsAsync extends AsyncTask<Void,Void,User>{
        User user;
        ServerCallback callback;
        public GetUserDataAsAsync(User user, ServerCallback callback){
            this.user = user;
            this.callback = callback;
        }
        @Override
        protected User doInBackground(Void... params) {
            //Will be used if we want to read some data from server
            BufferedReader reader = null;

            //Connection Handling
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "GetUserDetails?userName="+user.Username);
                //Opening the connection (Not setting or using CONNECTION_TIMEOUT)
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                //Data Read Procedure - Basically reading the data comming line by line
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while((line = reader.readLine()) != null) { //Read till there is something available
                    sb.append(line + "\n");     //Reading and saving line by line - not all at once
                }
                line = sb.toString();           //Saving complete data received in string, you can do it differently
                JSONObject jsonObject = new JSONObject(line);
                if(jsonObject.length()==0){
                    return null;
                }
                else{
                    String name = jsonObject.getString("Name");
                    String username = jsonObject.getString("Username");
                    String mobileNo = jsonObject.getString("MobileNo");
                    String licenseno = jsonObject.getString("LicensNo");
                    String vehicleType =jsonObject.getString("VehicleType");
                    String referredBy =jsonObject.getString("ReferredBy");
                    return new User(username,"",licenseno,vehicleType,name,mobileNo,referredBy);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();     //Closing the
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            callback.Done(returnedUser);
            super.onPostExecute(user);
        }
    }
}
