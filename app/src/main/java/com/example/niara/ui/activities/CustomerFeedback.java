package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.CustomerFeedbackModel;
import com.example.niara.Model.LoginRequest;
import com.example.niara.Model.LoginToken;
import com.example.niara.R;
import com.example.niara.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerFeedback extends AppCompatActivity {
    EditText textArea,fullname,email,phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feedback);

        textArea = (EditText) findViewById(R.id.feedback_cf);
        fullname=findViewById(R.id.fullname_cf);
        phone=findViewById(R.id.phone_cf);
        email=findViewById(R.id.email_cf);


        textArea.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;

            }
        });

    }


    public void submitForm(View view) {
        CustomerFeedbackModel customerFeedbackModel=new CustomerFeedbackModel();
        customerFeedbackModel.setName(fullname.getText().toString());
        customerFeedbackModel.setEmail(email.getText().toString());
        customerFeedbackModel.setPhone(phone.getText().toString());
        customerFeedbackModel.setDesc(textArea.getText().toString());

        sendFeedback(customerFeedbackModel);

    }

    private void sendFeedback(CustomerFeedbackModel customerFeedbackModel) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CustomerFeedbackModel> customerFeedbackCall=apiInterface.sendFeedback(customerFeedbackModel);
        customerFeedbackCall.enqueue(new Callback<CustomerFeedbackModel>() {
            @Override
            public void onResponse(Call<CustomerFeedbackModel> call, Response<CustomerFeedbackModel> response) {
                if (response.isSuccessful()){
                    Toast.makeText(CustomerFeedback.this,"Your Feedback has been sent",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CustomerFeedback.this,"Your Feedback couldn't be sent",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CustomerFeedbackModel> call, Throwable t) {
                Toast.makeText(CustomerFeedback.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();

            }
        });
    }
}