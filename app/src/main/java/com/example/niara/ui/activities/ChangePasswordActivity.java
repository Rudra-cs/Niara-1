package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.ChangePassword;
import com.example.niara.Model.CustomerFeedbackModel;
import com.example.niara.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText newpassword,oldpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        newpassword=findViewById(R.id.newpassword_cp);
        oldpassword=findViewById(R.id.oldpassword_cp);
    }

    public void submit(View view) {
        ChangePassword changePassword=new ChangePassword();
        changePassword.setOld_password(oldpassword.getText().toString());
        changePassword.setNew_password(newpassword.getText().toString());

        sendFeedback(changePassword);

    }

    private void sendFeedback(ChangePassword changePassword) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ChangePassword> changePasswordCall=apiInterface.sendPasswordChangeRequest(changePassword);
        changePasswordCall.enqueue(new Callback<ChangePassword>() {

            @Override
            public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {
                Log.d("res", "onResponse: ");
                if (response.isSuccessful()){
                    Toast.makeText(ChangePasswordActivity.this,"Your Password Has Been Changed",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ChangePasswordActivity.this,"Your Request couldn't be sent",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePassword> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
}