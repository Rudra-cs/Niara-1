package com.NiaraFoodJoint.niara.utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.NiaraFoodJoint.niara.R;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Common.booleanisConnectedtoInternet(context)){
            AlertDialog.Builder builder=new AlertDialog.Builder(context,R.style.fullscreenalert);
            View layout_dialogue= LayoutInflater.from(context).inflate(R.layout.check_internet_dialogue,null);
            builder.setView(layout_dialogue);

            AppCompatButton btnretry=layout_dialogue.findViewById(R.id.btn_retry);


            AlertDialog dialog= builder.create();
            dialog.show();
            dialog.setCancelable(false);

            btnretry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    onReceive(context,intent);
                }
            });
        }
    }
}
