package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;

import vn.edu.tdc.xfood.R;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.datamodels.User;

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
        Button registerButton = findViewById(R.id.btnRegister);
        registerButton.setText("Đang tải...");
        registerButton.setEnabled(false);

        //check less then 6 characters password
        if (username.length() < 6 || username.length() > 35) {
            showAlert("THÔNG BÁO", "Tên người dùng chỉ từ 6 đến 35 kí tự");
            registerButton.setText("Đăng ký");
            registerButton.setEnabled(true);
            return;
        }

        //check valid email
        if (!LoginActivity.VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()) {
            showAlert("THÔNG BÁO", "Email không hợp lệ");
            registerButton.setText("Đăng ký");
            registerButton.setEnabled(true);
            return;
        }

        //check less then 6 characters password
        if (password.length() < 6 || password.length() > 15) {
            showAlert("THÔNG BÁO", "Mật khẩu chỉ từ 6 đến 15 kí tự");
            registerButton.setText("Đăng ký");
            registerButton.setEnabled(true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("THÔNG BÁO", "Mật khẩu nhập lại không chính xác");
            registerButton.setText("Đăng ký");
            registerButton.setEnabled(true);
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

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
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
