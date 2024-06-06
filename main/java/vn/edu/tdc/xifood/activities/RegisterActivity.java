package vn.edu.tdc.xifood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.mydatamodels.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;

    private API<User> userAPI;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.conPass);

        SharePreference.setSharedPreferences(RegisterActivity.this);
        userAPI = new API<>(User.class, API.USER_TABLE_NAME);

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
        if (username.length() < 6 || username.length() > 36) {
            showAlert("THÔNG BÁO", "Tên người dùng chỉ từ 6 đến 36 kí tự");
            registerButton.setText("Đăng ký");
            registerButton.setEnabled(true);
            return;
        }
        if (email.length() > 255) {
            showAlert("THÔNG BÁO", "email không vượt quá 255 kí tự");
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
        if (password.length() < 6 || password.length() > 24) {
            showAlert("THÔNG BÁO", "Mật khẩu chỉ từ 6 đến 24 kí tự");
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

        userAPI.all(new API.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList list) {
                for (Object o: list ) {
                    User u = (User) o;

                    if (u.getEmail().equalsIgnoreCase(email)) {
                        //email already exist
                        showAlert("THÔNG BÁO", "Email đã tồn tại");
                        registerButton.setText("Đăng ký");
                        registerButton.setEnabled(true);
                        return;
                    }
                }

                // Mã hóa mật khẩu trước khi lưu vào SharedPreferences
                String hashedPassword = hashPassword(password);

                User user = new User();
                user.setFullName(username);
                user.setPassword(hashedPassword);
                user.setEmail(email);

                String key = userAPI.store(user,
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                //Save into local
                                SharePreference.store(SharePreference.USER_NAME, user.getFullName());
                                SharePreference.store(SharePreference.USER_EMAIL, user.getEmail());
                                SharePreference.store(SharePreference.USER_PASS, user.getPassword());

                                Toast.makeText(RegisterActivity.this, "Xin chào " + username, Toast.LENGTH_LONG).show();
                                showAlertAndNavigate("THÔNG BÁO", "Đăng kí thành công");
                            }
                        },
                        new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                showAlert("THÔNG BÁO", "Đăng ký thất bại :< Vui lòng thử lại");
                            }
                        },
                        new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                registerButton.setEnabled(true);
                                registerButton.setText("Đăng ký");
                            }
                        }
                );

                SharePreference.store(SharePreference.USER_TOKEN_KEY, key);
            }
        });
    }

    private static String hashPassword(String password) {
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
                        // Chuyển hướng sang trang home sau khi nhấn OK
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }
}
