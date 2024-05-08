package vn.edu.tdc.xifood.apis;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreference {
    private static String sharePreferenceName = "MY_PREFERENCE";
    public static final String USER_TOKEN_KEY = "USER_TOKEN_KEY";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_GENDER= "USER_GENDER";
    public static final String USER_DOB = "USER_DOB";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String USER_AVATAR = "USER_AVATAR";
    public static final String USER_PASS = "USER_PASS";
    public static final String USER_PERMISSION = "USER_PERMISSION";
    private static SharedPreferences sharedPreferences = null;
    public static void setSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(sharePreferenceName, Context.MODE_PRIVATE);
    }

    public static String find(String key) {
        return sharedPreferences.getString(key, "");
    }
    public static int findPermission() {
        return sharedPreferences.getInt(USER_PERMISSION, 0);
    }

    public static void store(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void store(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void destroy(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
