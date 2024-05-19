package vn.edu.tdc.xifood.apis;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class UserPreferences {
    private static final String PREF_NAME = "user_preferences";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GOOGLE_USERNAME = "google_username";
    private static final String KEY_GOOGLE_EMAIL = "google_email";

    private SharedPreferences preferences;

    public UserPreferences(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Lưu thông tin đăng nhập thông thường
    public void saveLoginCredentials(String username, String email) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Lấy thông tin đăng nhập thông thường
    public String getUsername() {
        return preferences.getString(KEY_USERNAME, "");
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, "");
    }

    // Kiểm tra xem đã đăng nhập thông thường chưa
    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(getUsername()) && !TextUtils.isEmpty(getEmail());
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
    public boolean checkLogin(String username, String password) {
        String savedUsername = getUsername();
        return savedUsername.equals(username);
    }

    // Xóa thông tin đăng nhập thông thường
    public void clearLoginCredentials() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_EMAIL);
        editor.apply();
    }

    // Xóa thông tin đăng nhập bằng Google
    public void clearGoogleSignIn() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_GOOGLE_USERNAME);
        editor.remove(KEY_GOOGLE_EMAIL);
        editor.apply();
    }
}
