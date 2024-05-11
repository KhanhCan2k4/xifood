package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import java.security.MessageDigest;
import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.apis.UserPreferences;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.conPass);

        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
        confirmPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());


        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
            }
        });

        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerAccount() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            showAlert("LỖI", "Vui lòng điền đầy đủ thông tin");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("LỖI", "Mật khẩu nhập lại không chính xác");
            return;
        }
        UserAPI.all(new UserAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<User> users) {
                if (users != null) {
                    for (User u : users) {
                        if (u.getEmail().equalsIgnoreCase(email)) {
                            //email already exist
                            showAlert("THÔNG BÁO", "Email đã tồn tại");
                            registerButton.setText("Đăng ký");
                            registerButton.setEnabled(true);
                            return;
                        }
                    }
                }

                // Mã hóa mật khẩu trước khi lưu vào SharedPreferences
                String hashedPassword = hashPassword(password);

                User user = new User();
                user.setFullName(username);
                user.setPassword(hashedPassword);
                user.setAvatar("avatars/default.jpg");
                user.setGender(AccountActivity.GENDER_DEFAULT);
                user.setEmail(email);
                user.setBio("");
                user.setDayOfBirth("");

                UserAPI.store(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //khoi tao san cac key cua local
                            SharePreference.init();
                            //Save into local
                            SharePreference.store(SharePreference.USER_TOKEN_KEY, user.getKey());
                            SharePreference.store(SharePreference.USER_NAME, user.getFullName());
                            SharePreference.store(SharePreference.USER_EMAIL, user.getEmail());
                            SharePreference.store(SharePreference.USER_PASS, user.getPassword());
                            SharePreference.store(SharePreference.USER_GENDER, AccountActivity.GENDER_DEFAULT);

                            Toast.makeText(RegisterActivity.this, "Xin chào" + username, Toast.LENGTH_LONG).show();
                            showAlertAndNavigate("THÔNG BÁO", "Đăng kí thành công");
                        }
                    })
                    .addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            showAlert("THÔNG BÁO", "Đăng ký thất bại :< Vui lòng thử lại");
                            registerButton.setEnabled(true);
                            registerButton.setText("Đăng ký");
                        }
                    });
            }
        });
    }

        // Mã hóa mật khẩu trước khi lưu vào SharedPreferences
        String hashedPassword = hashPassword(password);

        //Lưu vào Shaped
        UserPreferences userPrefs = new UserPreferences(this);
        userPrefs.saveLoginCredentials(username, hashedPassword);

        showAlertAndNavigate("Thông báo", "Đăng kí thành công. Đăng nhập ngay ?");
    }
    private String hashPassword(String password) {
        try {
            // Sử dụng thuật toán SHA-256 để mã hóa mật khẩu
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));

            // Chuyển đổi byte array sang dạng hex để lưu trữ trong SharedPreferences
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showAlertAndNavigate(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        navigateToLogin(); // Chuyển hướng sang trang đăng nhập sau khi nhấn OK
                    }
                })
                .show();
    }
}
