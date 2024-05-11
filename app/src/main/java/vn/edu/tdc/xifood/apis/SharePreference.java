package vn.edu.tdc.xifood.apis;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreference {
    private static final String sharePreferenceName = "MY_PREFERENCE";
    public static final String USER_TOKEN_KEY = "USER_TOKEN_KEY";
    private static SharedPreferences sharedPreferences = null;

    public static void setSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(sharePreferenceName, Context.MODE_PRIVATE);
    }

    public static String find(String key) {
        return sharedPreferences.getString(key, "");
    }
    public static void store(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void destroy(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void init() {
        SharePreference.store(SharePreference.USER_TOKEN_KEY, "");
        SharePreference.store(SharePreference.USER_NAME, "");
        SharePreference.store(SharePreference.USER_EMAIL, "");
        SharePreference.store(SharePreference.USER_GENDER, "");
        SharePreference.store(SharePreference.USER_DOB, "");
        SharePreference.store(SharePreference.USER_AVATAR, "");
        SharePreference.store(SharePreference.USER_PHONE, "");
        SharePreference.store(SharePreference.USER_PASS, "");

    }
}
