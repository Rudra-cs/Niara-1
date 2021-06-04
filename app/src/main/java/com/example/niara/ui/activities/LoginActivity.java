package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.LoginRequest;
import com.example.niara.Model.LoginToken;
import com.example.niara.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "PrefsFile";
    private SharedPreferences prefManager;
    private SharedPreferences.Editor editor;
//    public EditText namelogin;
//    public EditText passwordlogin;
    public String token;
    public EditText passwordlogin,namelogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void gotohome() {
        SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.PREFS_NAME,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
         namelogin=findViewById(R.id.et_usernameLogin);
         passwordlogin=findViewById(R.id.et_passwordLogin);

        editor.putBoolean("hasLoggedIn",true);
        editor.commit();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public void movetosignup(View view) {
        Intent intent=new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);

    }

    public void loginclicked(View view) {
        LoginRequest loginRequest=creatLoginRequest();
        loginUsertohome(loginRequest);
    }
    public LoginRequest creatLoginRequest(){
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setUsername(namelogin.getText().toString());
        loginRequest.setPassword(passwordlogin.getText().toString());
        return loginRequest;


    }
    private void loginUsertohome(LoginRequest loginRequest){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginToken> loginTokenCall=apiInterface.loginUser(loginRequest);
        loginTokenCall.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                if ((response.isSuccessful())){
                    token=response.body().getToken();
                    if(token!=null){
                        gotohome();
                    }else{
                        Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,"login Failed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"User login failed"+t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    

}