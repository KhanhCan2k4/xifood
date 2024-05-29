package vn.edu.tdc.xifood.apis;

import android.content.Context;
import android.content.SharedPreferences;

import vn.edu.tdc.xifood.datamodels.User;

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
    public static final String CART_KEY = "CART_KEY";
    public static final String LIKED_PRODUCTS_KEY = "LIKED_PRODUCT_KEY";
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

    //Hàm truy vấn dữ liệu của người dùng từ local
    public static User getUser() {
        User user = new User();
        user.setKey(SharePreference.find(SharePreference.USER_TOKEN_KEY));
        user.setFullName(SharePreference.find(SharePreference.USER_NAME));
        user.setEmail(SharePreference.find(SharePreference.USER_EMAIL));
        user.setAvatar(SharePreference.find(SharePreference.USER_AVATAR));
        user.setDayOfBirth(SharePreference.find(SharePreference.USER_DOB));
        user.setGender(SharePreference.find(SharePreference.USER_GENDER));
        user.setPassword(SharePreference.find(SharePreference.USER_PASS));
        user.setPermistion(SharePreference.findPermission());
        return user;
    }
    // Hàm xóa dữ liệu người dùng
    public static void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
