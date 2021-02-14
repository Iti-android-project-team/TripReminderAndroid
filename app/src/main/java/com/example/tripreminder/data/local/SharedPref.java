package com.example.tripreminder.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

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


    public static void setUserEmail(String email){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user-email",email);
        editor.apply();
    }
    public static String getUserEmail(){
        return  pref.getString("user-email"," ");
    }

    public static void setNotes(String notes){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("notes",notes);
        editor.apply();
    }

    public static String getNotes(){
        return  pref.getString("notes"," ");
    }

    public static void setFloatingNotes(String notes){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("floatingNotes",notes);
        editor.apply();
    }

    public static String getFloatingNotes(){
        return  pref.getString("floatingNotes"," ");
    }


    public static void setLogoutSocial(GoogleSignInAccount account){
        account = null;

    }

    public static void setRegisterWithFirebase(boolean login){
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("register-firebase",login);
        editor.apply();
    }

    public static Boolean checkRegisterWithFirebase(){
        return  pref.getBoolean("register-firebase",false);
    }

    public static void setLoginWithFirebase(boolean login){
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("login-firebase",login);
        editor.apply();
    }

    public static Boolean checkLoginWithFirebase(){
        return  pref.getBoolean("login-firebase",false);
    }

}
