package com.example.belatarr;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Session {

    private static  final  String KEY_ID="ID";
    private static  final  String KEY_NAME="NAME";
    private static  final  String KEY_TYPE="TYPE";
    private static  final  String KEY_TEAM="TEAM";
    private static final String SHARED_PREF_NAME = "SESSION";

    private static final String LAST_USER_INFO = "USER_INFO";
    private static final String LAST_USER_NAME = "USER_NAME";
    private static final String LAST_TYPE_TYPE = "USER_TYPE";
    private static final String LAST_USER_TEAM = "USER_TEAM";

    private static String IS_CONNECTED = "false";


    private static Session sharedInstance;
    private static Context _context;

    private Session(Context context) {
        _context= context ;
    }

    public static synchronized Session getInstance(Context context) {
        if (sharedInstance == null) {
            sharedInstance = new Session(context);
        }
        return sharedInstance;
    }

    public void userLogin(User user) {

        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_NAME, user.getUsername());
        editor.putString(KEY_TYPE, user.getType());
        editor.putString(KEY_TEAM, user.getTeam());
        editor.apply();
        IS_CONNECTED="true";
    }

    public void lastUserLogin(User user) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(LAST_USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_USER_NAME, user.getUsername());
        editor.putString(LAST_TYPE_TYPE, user.getType());
        editor.putString(LAST_USER_TEAM, user.getTeam());
        editor.apply();
    }

    public User getLastUserLogin() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(LAST_USER_INFO, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(LAST_USER_NAME, null),
                sharedPreferences.getString(LAST_TYPE_TYPE, null)
        );
    }

    public String getIsConnected() {

        return IS_CONNECTED;
    }



    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null) != null;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, 1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_TYPE, null),
                sharedPreferences.getString(KEY_TEAM,null)

        );
    }
    public void logout() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        _context.startActivity(new Intent(_context, MainActivity.class));
    }



}
