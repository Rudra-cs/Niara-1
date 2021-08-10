package com.example.niara.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.niara.Adapters.CustomerInfoAdapter;
import com.example.niara.Adapters.CustomerInfoAdapter1;
import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.CustomerInfo;
import com.example.niara.R;
import com.example.niara.ui.activities.CreateCusstomerInfoActivity;
import com.example.niara.ui.activities.ChangePasswordActivity;
import com.example.niara.ui.activities.CustomerFeedback;
import com.example.niara.ui.activities.LoginActivity;
import com.example.niara.ui.activities.MainActivity;
import com.example.niara.ui.activities.PaymentActivity;
import com.example.niara.ui.activities.PrivacyPolicy;
import com.example.niara.ui.activities.TermsAndConditions;
import com.example.niara.utils.SessionManager;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsFragment extends Fragment {
    private int i,j;
    private LinearLayout privacypolicy,termsConditions;
    private RecyclerView recyclerView;
    private List<CustomerInfo> customerInfolist;
    private CustomerInfoAdapter1 customerInfoAdapter;
    private TextView changepasswordtv,usernameSettings,feedback,addAddress;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LinearLayout logoutsection,ll_rcorder,changepasswordsection,addresssection,feedbacksection,addresslistsection,arrowaddressright,arrowaddressdown;

    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        changepasswordtv=view.findViewById(R.id.tv_changepassword_settings);
        feedback=view.findViewById(R.id.tv_feedback);
        addAddress=view.findViewById(R.id.tv_Addresses);
        usernameSettings=view.findViewById(R.id.tv_username_settings);
        logoutsection=view.findViewById(R.id.logout_section);
        changepasswordsection=view.findViewById(R.id.password_section);
        addresssection=view.findViewById(R.id.myaddresses);
        feedbacksection=view.findViewById(R.id.feedback_section);
        addresslistsection=view.findViewById(R.id.addresseslistSection);
        arrowaddressdown=view.findViewById(R.id.arrowdownaddress);
        arrowaddressright=view.findViewById(R.id.arrowaddress);
        ll_rcorder=view.findViewById(R.id.ll_rcorder);
        customerInfolist = new ArrayList<>();

        termsConditions=view.findViewById(R.id.TermsAndConditions);
        privacypolicy=view.findViewById(R.id.PrivacyPolicy);

        termsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TermsAndConditions.class));
            }
        });
        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PrivacyPolicy.class));
            }
        });

        Runnable objRunnable=new Runnable() {
            @Override
            public void run() {
                //
                loadAddress();
            }
        };

        Thread objBgThread=new Thread(objRunnable);
        objBgThread.start();

        recyclerView = view.findViewById(R.id.rc_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        addresslistsection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrowaddressright.setVisibility(View.GONE);
                arrowaddressdown.setVisibility(View.VISIBLE);
                ll_rcorder.setVisibility(View.VISIBLE);
            }
        });
        arrowaddressdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrowaddressright.setVisibility(View.VISIBLE);
                arrowaddressdown.setVisibility(View.GONE);
                ll_rcorder.setVisibility(View.GONE);
            }
        });


        logoutsection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext()).setTitle("Logout")
                        .setMessage("Are you sure you want to Logout")
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SessionManager sessionManager=new SessionManager(getContext());
                                sessionManager.logoutuserfromsession();
                                startActivity(new Intent(getContext(),LoginActivity.class));
                                getActivity().finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

            }
        });

        changepasswordsection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Login in to the site with your credentials and then change your password",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), ChangePasswordActivity.class));
            }
        });

        feedbacksection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CustomerFeedback.class));
            }
        });

        addresssection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateCusstomerInfoActivity.class));
            }
        });
        SessionManager sessionManager=new SessionManager(getContext());
        String username=sessionManager.getUsername();
        usernameSettings.setText(username);

        return view;
    }

    private void loadAddress() {
        if (customerInfolist != null) {
            customerInfolist.clear();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<CustomerInfo>> address = apiInterface.getAllCustomers();
        address.enqueue(new Callback<List<CustomerInfo>>() {
            @Override
            public void onResponse(Call<List<CustomerInfo>> call, Response<List<CustomerInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SessionManager sessionManager = new SessionManager(getContext());
                    int userid = sessionManager.getUserid();
                    for (i = 0; i < response.body().size(); i++) {
                        if (String.valueOf(userid) == String.valueOf((response.body()).get(i).getUser())) {
                            customerInfolist.add(response.body().get(i));
                        }
                    }
                    displayAddresListInRecyclerView(customerInfolist);


                } else {
                    Toast.makeText(getContext(), "address not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CustomerInfo>> call, Throwable t) {
                Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void displayAddresListInRecyclerView(List<CustomerInfo> customerInfolist) {
        List list = new ArrayList(customerInfolist);
        if (customerInfolist!=null){
            Collections.reverse(list);
            Log.d("list123", String.valueOf(list));
            customerInfoAdapter= new CustomerInfoAdapter1(getContext(),list);
            recyclerView.setAdapter(customerInfoAdapter);
        }
    }
}