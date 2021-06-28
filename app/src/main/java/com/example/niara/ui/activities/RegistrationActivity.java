package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.UserRequest;
import com.example.niara.Model.UserResponse;
import com.example.niara.R;
import com.example.niara.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmpassword;
    private String str;
    public String token;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username=findViewById(R.id.et_username);
        email=findViewById(R.id.et_email);
        password=findViewById(R.id.et_password);
        confirmpassword=findViewById(R.id.et_confirmpassword);

    }
    private boolean validateUsername() {
        String val = username.getText().toString().trim();
        String checkspaces = "Aw{1,20}z";

        if (val.isEmpty()) {
            username.setError("Field can not be empty");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username is too large!");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getText().toString().trim();
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
            password.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password should contain 4 characters!");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void movetologin(View view) {
        Intent intent=new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);

    }



    public void registerUser1(View view) {
        UserRequest userRequest1=createUserRequest();
        registerUser(userRequest1);

    }


    public UserRequest createUserRequest(){
        UserRequest userRequest=new UserRequest();
        userRequest.setUsername(username.getText().toString().trim());
        userRequest.setEmail(email.getText().toString());
        userRequest.setPassword(password.getText().toString().trim());
        return userRequest;
    }
    public void gotohome() {
        SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.PREFS_NAME,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
        finish();


    }
    public void registerUser(UserRequest userRequest){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> userResponseCall=apiInterface.registerUser(userRequest);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                response.body();
                if ((response.isSuccessful())){
                    token=response.body().getToken();
                    Log.d("userdetails", String.valueOf(response.body().getUser()));
                    id=response.body().getUser().getId();
                    if(token!=null){
                        Button btn = (Button)findViewById(R.id.register_button);
                        btn.setEnabled(false);
                        SessionManager sessionManager=new SessionManager(RegistrationActivity.this);
                        sessionManager.createloginsession(token,username.getText().toString(),id);
                        gotohome();
                    }else{
                        Toast.makeText(RegistrationActivity.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegistrationActivity.this,"Request Failed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this,"Error Occured"+t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}