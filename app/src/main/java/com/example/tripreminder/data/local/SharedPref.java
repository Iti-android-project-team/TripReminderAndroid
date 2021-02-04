package com.example.tripreminder.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPref {
    private static SharedPreferences pref = null;
    private SharedPref(){}

    public static SharedPreferences createPrefObject(Context context){
        if(pref == null){
            pref = context.getSharedPreferences("Trip",MODE_PRIVATE);
        }
        return pref;
    }

    public static void setLogin(boolean login){
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("login",login);
        editor.apply();
    }
    public static Boolean checkLogin(){
        return  pref.getBoolean("login",false);
    }

}