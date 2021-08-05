package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.LoginRequest;
import com.example.niara.Model.LoginToken;
import com.example.niara.Model.UserInfo;
import com.example.niara.R;
import com.example.niara.utils.NetworkChangeListener;
import com.example.niara.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "PrefsFile";
    private ArrayList<UserInfo> userInfoArrayList;
    private String token;
    private int id,i;
    private EditText passwordlogin,namelogin;
    private String rudra,rudrausername;
    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        namelogin=findViewById(R.id.et_usernameLogin);
        passwordlogin=findViewById(R.id.et_passwordLogin);

        SessionManager sessionManager=new SessionManager(LoginActivity.this);
        boolean isloggedin=sessionManager.checkLogin();
        if(isloggedin){
            gotohome();
        }
    }

    private void gotohome() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public void movetosignup(View view) {
        Intent intent=new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    private boolean validateUsername() {
        String val = namelogin.getText().toString().trim();
        String checkspaces = "Aw{1,20}z";

        if (val.isEmpty()) {
            namelogin.setError("Field can not be empty");
            return false;
        }else {
            namelogin.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = passwordlogin.getText().toString().trim();
        String checkPassword = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=S+$)" +           //no white spaces
                ".{6,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            passwordlogin.setError("Field can not be empty");
            return false;
        } else {
            passwordlogin.setError(null);
            return true;
        }
    }

    public void loginclicked(View view) {
        if (validateUsername() && validatePassword()){
            Toast.makeText(LoginActivity.this,"Please wait for a moment while logging you in",Toast.LENGTH_LONG).show();
            LoginRequest loginRequest=creatLoginRequest();
            loginUsertohome(loginRequest);
        }

    }

    private LoginRequest creatLoginRequest(){
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setUsername(namelogin.getText().toString());
        loginRequest.setPassword(passwordlogin.getText().toString());
        return loginRequest;
    }

    private void loginUsertohome(LoginRequest loginRequest){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginToken> loginTokenCall=apiInterface.loginUser(loginRequest.getUsername(),loginRequest.getPassword());
        loginTokenCall.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {

                if ((response.isSuccessful())){
                    token=response.body().getToken();
                    if(token!=null){
                        getuseridaftertoken();
                        Button btn = (Button)findViewById(R.id.login_button);
                        btn.setEnabled(false);
                    }else{
                        Toast.makeText(LoginActivity.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,"Login error occured,try again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Login error occured,try again",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getuseridaftertoken() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<UserInfo>> userinfo = apiInterface.getuserdetails();
        userinfo.enqueue(new Callback<ArrayList<UserInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<UserInfo>> call, Response<ArrayList<UserInfo>> response) {
                if (response.isSuccessful()){
                    userInfoArrayList=response.body();
                    for (i=0;i<userInfoArrayList.size();i++){
                        rudra=namelogin.getText().toString().trim();
                        rudrausername=userInfoArrayList.get(i).getUsername();
                        Boolean b=rudra.equals(rudrausername);

                        if (b!=false){
                            id=userInfoArrayList.get(i).getId();
                            SessionManager sessionManager=new SessionManager(LoginActivity.this);
                            sessionManager.createloginsession(namelogin.getText().toString(),id);
                            gotohome();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserInfo>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.finish();
    }

    public void movetofeedback(View view) {
        startActivity(new Intent(LoginActivity.this, CustomerFeedback.class));
    }

    @Override
    protected void onStart() {
        IntentFilter filter1=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter1);
        super.onStart();
    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}