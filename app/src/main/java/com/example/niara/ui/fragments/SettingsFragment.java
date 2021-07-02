package com.example.niara.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.niara.R;
import com.example.niara.ui.activities.CreateCusstomerInfoActivity;
import com.example.niara.ui.activities.ChangePasswordActivity;
import com.example.niara.ui.activities.CustomerFeedback;
import com.example.niara.ui.activities.LoginActivity;
import com.example.niara.utils.SessionManager;


public class SettingsFragment extends Fragment {
    public TextView changepasswordtv,usernameSettings,feedback,addAddress;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

        SessionManager sessionManager=new SessionManager(getContext());
        String username=sessionManager.getUsername();
        int userid=sessionManager.getUserid();
        usernameSettings.setText(username);
        TextView id=view.findViewById(R.id.tv_userid_settings);
        id.setText(String.valueOf(userid));

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateCusstomerInfoActivity.class));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CustomerFeedback.class));
            }
        });

        changepasswordtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Login in to the site and then change your password",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), ChangePasswordActivity.class));
            }
        });

        TextView tvlogout=view.findViewById(R.id.tv_logout);
        tvlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext()).setTitle("Alert")
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


        return view;
    }
}