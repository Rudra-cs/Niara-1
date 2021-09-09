package com.NiaraFoodJoint.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.NiaraFoodJoint.niara.Api.ApiClient;
import com.NiaraFoodJoint.niara.Api.ApiInterface;
import com.NiaraFoodJoint.niara.Model.CustomerFeedbackModel;
import com.NiaraFoodJoint.niara.R;
import com.NiaraFoodJoint.niara.utils.NetworkChangeListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customerFeedback2 extends AppCompatActivity {

    private EditText textArea,fullname,email,phone;

    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feedback2);

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
                    Toast.makeText(customerFeedback2.this,"Your Feedback has been sent",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(customerFeedback2.this,"Your Feedback couldn't be sent",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerFeedbackModel> call, Throwable t) {
                Toast.makeText(customerFeedback2.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();

            }
        });
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(customerFeedback2.this, LoginActivity.class));
        finish();
    }
}