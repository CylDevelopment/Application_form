package com.example.saral_suvidha.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManagement {

    public static final int PRIVATE_MODE = 0;
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_MOBILE_NO = "mobile_no";
    public static final String KEY_PIN = "pin";
    public static final String IS_NOT_NEW = "new";
    public static final String ISLOGIN = "islogin";
    public static final String PREF_NAME = "userdata";
    public static final String PREF_NAME1 = "pinData";

    SharedPreferences pref, pref1;
    SharedPreferences.Editor editor, editor1;

    Context context;

    public SessionManagement(Context ctx) {
        this.context = ctx;
        pref = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref1 = ctx.getSharedPreferences(PREF_NAME1, PRIVATE_MODE);
        editor = pref.edit();
        editor1 = pref1.edit();
    }

    public void createPinNew(String pin) {
        editor1.putString(KEY_PIN, pin);
        editor1.putBoolean(IS_NOT_NEW, true);
        editor1.commit();
    }

    public void createSession(String username, String password, String mobile_no) {
        editor.putBoolean(ISLOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_MOBILE_NO, mobile_no);
        editor.commit();
    }


    public boolean isNotNew() {
        return pref1.getBoolean(IS_NOT_NEW, false);
    }

    /*public boolean isLogin() {
        return pref.getBoolean(ISLOGIN, false);
    }*/

    public HashMap<String, String> getPin() {
        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_PIN, pref1.getString(KEY_PIN, null));
        return map;
    }

    /*public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_MOBILE_NO, pref.getString(KEY_MOBILE_NO, null));
        return user;
    }*/

}
