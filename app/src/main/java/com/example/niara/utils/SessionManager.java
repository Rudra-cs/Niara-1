package com.example.niara.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences userSessions;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN="ISLOGGEDIN";
    public static final String KEY_FULLNAME="FULLNAME";//registration fullname
    public static final String KEY_TOKEN="TOKEN";
    public static final String USERNAME="USERNAME";
    public static final String USERID="USERID";

    public SessionManager(Context _context){
        context=_context;
        userSessions=context.getSharedPreferences("userLoginSessions",Context.MODE_PRIVATE);
        editor=userSessions.edit();
    }

    public void createloginsession(String token,String username,int userid){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_TOKEN,token);
        editor.putString(USERNAME,username);
        editor.putInt(USERID,userid);
        editor.commit();
    }

    public HashMap<String,String> getuserdetail(String username, Object o){
        HashMap<String,String> userdata=new HashMap<String, String>();
        userdata.put(KEY_TOKEN,userSessions.getString(KEY_TOKEN,null));
        return userdata;

    }
    public boolean checkLogin(){
        if(userSessions.getBoolean(IS_LOGIN,false)){
            return true;
        }
        else {
            return false;
        }
    }
    public void logoutuserfromsession(){
        editor.clear();
        editor.commit();
    }

    public String getUsername(){
        return userSessions.getString(USERNAME,null);
    }

    public int getUserid(){
        return userSessions.getInt(USERID,-1);
    }
}
