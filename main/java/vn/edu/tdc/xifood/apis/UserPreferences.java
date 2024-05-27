package vn.edu.tdc.xifood.apis;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class UserPreferences {
    private static final String PREF_NAME = "user_preferences";
    private static final String KEY_GOOGLE_USERNAME = "google_username";
    private static final String KEY_GOOGLE_EMAIL = "google_email";

    private SharedPreferences preferences;

    public UserPreferences(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Lưu thông tin đăng nhập bằng Google
    public void saveGoogleSignIn(String googleUsername, String googleEmail) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_GOOGLE_USERNAME, googleUsername);
        editor.putString(KEY_GOOGLE_EMAIL, googleEmail);
        editor.apply();
    }

    // Lấy thông tin đăng nhập bằng Google
    public String getGoogleUsername() {
        return preferences.getString(KEY_GOOGLE_USERNAME, "");
    }

    public String getGoogleEmail() {
        return preferences.getString(KEY_GOOGLE_EMAIL, "");
    }

    // Kiểm tra xem đã đăng nhập bằng Google chưa
    public boolean isGoogleSignedIn() {
        return !TextUtils.isEmpty(getGoogleUsername()) && !TextUtils.isEmpty(getGoogleEmail());
    }
}
