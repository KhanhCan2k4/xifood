package vn.edu.tdc.xifood.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.regex.Pattern;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.mydatamodels.User;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private static final int RC_SIGN_IN = 9001;
    private API<User> userAPI;
    private String alert = "đăng nhập không thành công! vui lòng kiểm tra lại email hoặc mật khẩu!";
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        SharePreference.setSharedPreferences(LoginActivity.this);
        //check used to login -> navigate to main
        //accept login
        userAPI = new API<>(User.class, API.USER_TABLE_NAME);

        String key = SharePreference.find(SharePreference.USER_TOKEN_KEY);

        if (!key.isEmpty()) {
            userAPI.find(key, new API.FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    if (object != null) {
                        User user = (User) object;
                        Log.d("TAG", "onCallback: user: " + user.getKey() + user.getFullName());
                        saveUserToLocal(user);
                        Toast.makeText(LoginActivity.this, "Xin chào " + user.getFullName(), Toast.LENGTH_LONG).show();
                        navigateToMainActivity();
                    }
                }
            });
        }

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());

        findViewById(R.id.btnRegister).setOnClickListener(v -> navigateToRegister());
        findViewById(R.id.btnSignIn).setOnClickListener(v -> login());
    }

    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void login() {
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Button btnLogin = findViewById(R.id.btnSignIn);
        btnLogin.setText("Đang tải...");
        btnLogin.setEnabled(false);

        //check valid email
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()) {
            showAlert("THÔNG BÁO", alert);
            btnLogin.setText("Đăng nhập");
            btnLogin.setEnabled(true);
            return;
        }

        //check less then 6 characters password
        if (password.length() < 6 || password.length() > 15) {
            showAlert("THÔNG BÁO", alert);
            btnLogin.setText("Đăng nhập");
            btnLogin.setEnabled(true);
            return;
        }

        userAPI.all(new API.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList list) {
                for (Object o : list) {
                    User u = (User) o;

                    if (u.getEmail().equalsIgnoreCase(email) && checkPassword(password, u.getPassword())) {
                        saveUserToLocal(u);
                        Toast.makeText(LoginActivity.this, "Xin chào " + u.getFullName(), Toast.LENGTH_LONG).show();
                        navigateToMainActivity();
                    }
                }

                //login failed
                btnLogin.setText("Đăng nhập");
                btnLogin.setEnabled(true);
                showAlert("THÔNG BÁO", alert);
            }
        });
    }

    public static void saveUserToLocal(User user) {
        //existing users with true data
        SharePreference.store(SharePreference.USER_TOKEN_KEY, user.getKey());
        SharePreference.store(SharePreference.USER_NAME, user.getFullName());
        SharePreference.store(SharePreference.USER_EMAIL, user.getEmail());
        SharePreference.store(SharePreference.USER_GENDER, user.getGender());
        SharePreference.store(SharePreference.USER_DOB, user.getDayOfBirth());
        SharePreference.store(SharePreference.USER_PHONE, user.getPhoneNumber());
        SharePreference.store(SharePreference.USER_PASS, user.getPassword());
        SharePreference.store(SharePreference.USER_PERMISSION, user.getPermistion());
    }

    public static boolean checkPassword(String inputPassword, String storedHash) {
        return BCrypt.checkpw(inputPassword, storedHash);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showAlert(String title, String message) {
        if (!isFinishing()) { // Kiểm tra xem activity có đang kết thúc không
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
    }
}
