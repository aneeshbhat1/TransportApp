package com.example.aneesh.transportapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {
    UserLocalStore userLocalStrore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button registerButton = (Button) findViewById(R.id.btnRegister);
        registerButton.setOnClickListener(this);
        userLocalStrore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnRegister:
                EditText name = (EditText) findViewById(R.id.reg_fullname);
                EditText password = (EditText) findViewById(R.id.reg_password);
                EditText userName = (EditText) findViewById(R.id.reg_username);
                Spinner vehicleType = (Spinner) findViewById(R.id.reg_vehicletype);
                EditText licenseNumber = (EditText) findViewById(R.id.reg_licensenumber);
                EditText mobileNummber = (EditText) findViewById(R.id.reg_mobileNumber);
                EditText referredBy = (EditText) findViewById(R.id.reg_referredby);
                User newUser = new User(userName.getText().toString(),password.getText().toString(),
                        licenseNumber.getText().toString(),
                        vehicleType.getSelectedItem().toString(),name.getText().toString(),
                        mobileNummber.getText().toString(),referredBy.getText().toString());

                userLocalStrore.saveUserData(newUser);
                DALWrapper dalWrapper = new DALWrapper(this,this);
                dalWrapper.SaveUserData(newUser, new ServerCallback() {
                    @Override
                    public void Done(User returnedUser) {
                        Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    public void onStartTransaction(View view) {
        PaytmPGService Service = PaytmPGService.getStagingService();


        //Kindly create complete Map and checksum on your server side and then put it here in paramMap.
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID" , "WorldP64425807474247");
        paramMap.put("ORDER_ID" , "TestMerchant000111007");
        paramMap.put("CUST_ID" , "mohit.aggarwal@paytm.com");
        paramMap.put("INDUSTRY_TYPE_ID" , "Retail");
        paramMap.put("CHANNEL_ID" , "WAP");
        paramMap.put("TXN_AMOUNT" , "1");
        paramMap.put("WEBSITE" , "worldpressplg");
        paramMap.put("CALLBACK_URL" , "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
        paramMap.put("CHECKSUMHASH" , "w2QDRMgp1/BNdEnJEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
        PaytmOrder Order = new PaytmOrder(paramMap);


        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                        Toast.makeText(getBaseContext(), "Some error ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction : " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction response "+inResponse.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() {
                        // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }
}
