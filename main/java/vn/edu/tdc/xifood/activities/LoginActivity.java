package vn.edu.tdc.xifood.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.regex.Pattern;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.activities.MainActivity;
import vn.edu.tdc.xifood.activities.RegisterActivity;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.apis.UserPreferences;
import vn.edu.tdc.xifood.datamodels.User;
import vn.edu.tdc.xifood.staffProcessing.MainStaffActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private String alert="đăng nhập không thành công! vui lòng kiểm tra lại email hoặc mật khẩu!";
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        SharePreference.setSharedPreferences(LoginActivity.this);
        //check used to login -> navigate to main
        if (!SharePreference.find(SharePreference.USER_TOKEN_KEY).isEmpty()) {
//            Log.d("TAG", "onCreate: used to login");
            String userName = SharePreference.find(SharePreference.USER_NAME);
            Toast.makeText(LoginActivity.this, "Xin chào " + userName, Toast.LENGTH_LONG).show();

            //check permisstion
            navigateToMainActivity();
        }

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());

        findViewById(R.id.btnRegister).setOnClickListener(v -> navigateToRegister());
        findViewById(R.id.btnSignIn).setOnClickListener(v -> login());
        findViewById(R.id.loginWithGoogle).setOnClickListener(v -> dangNhapGoogle());
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

        //accept login
        UserAPI.all(new UserAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<User> users) {
                for (User user : users) {
                    if (user.getEmail().equals(email) &&
                            checkPassword(password, user.getPassword())) {
                        //existing users with true data
                        SharePreference.setSharedPreferences(LoginActivity.this);
                        SharePreference.store(SharePreference.USER_TOKEN_KEY, user.getKey());
                        SharePreference.store(SharePreference.USER_NAME, user.getFullName());
                        SharePreference.store(SharePreference.USER_EMAIL, user.getEmail());
                        SharePreference.store(SharePreference.USER_GENDER, user.getGender());
                        SharePreference.store(SharePreference.USER_DOB, user.getDayOfBirth());
                        SharePreference.store(SharePreference.USER_AVATAR, user.getAvatar());
                        SharePreference.store(SharePreference.USER_PHONE, user.getPhoneNumber());
                        SharePreference.store(SharePreference.USER_PASS, user.getPassword());
                        SharePreference.store(SharePreference.USER_PERMISSION, user.getPermistion());

                        //check permisstion
//                        Log.d("TAG", "login: success");
                        if (user.getPermistion() == UserAPI.STAFF_PERMISSION) { //is staff
                          //  navigateToMainActivityForStaff();
                            Intent intent = new Intent(LoginActivity.this, MainStaffActivity.class);
                            startActivity(intent);
                            finish();
                        } else { //is normal user
                            navigateToMainActivity();
                        }
                    }
                }

                //login failed
                btnLogin.setText("Đăng nhập");
                btnLogin.setEnabled(true);
                showAlert("THÔNG BÁO", alert);
            }
        });
    }

    public boolean checkPassword(String inputPassword, String storedHash) {
        return BCrypt.checkpw(inputPassword, storedHash);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToMainActivityForStaff() {
        Intent intent = new Intent(LoginActivity.this, MainStaffActivity.class);
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

    private void signInWithGoogle() {
        // Khởi tạo GoogleSignInOptions
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // Yêu cầu lấy thông tin email của người dùng
                .build();

        // Khởi tạo GoogleSignInClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Gửi Intent để yêu cầu đăng nhập Google
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    private void dangNhapGoogle() {
        Toast.makeText(LoginActivity.this, "Tính năng đang được cập nhật", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Đăng nhập thành công, xử lý kết quả đăng nhập
                handleSignInResult(data);
            } else {
                // Đăng nhập không thành công
                Log.e("GoogleSignIn", "Đăng nhập không thành công");
                showAlert("LỖI", alert);
            }
        }
    }

    private void handleSignInResult(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(googleSignInAccount -> {
                    // Xử lý đăng nhập thành công
                    onGoogleSignInSuccess(googleSignInAccount);
                })
                .addOnFailureListener(e -> {
                    // Xử lý đăng nhập thất bại
                    Log.e("GoogleSignIn", alert, e);
                    showAlert("LỖI", alert);
                });
    }

    private void onGoogleSignInSuccess(GoogleSignInAccount account) {
        String googleUsername = account.getDisplayName(); // Lấy tên hiển thị của người dùng Google
        String googleEmail = account.getEmail(); // Lấy địa chỉ email của người dùng Google

        // Lưu thông tin đăng nhập vào SharedPreferences
        UserPreferences userPrefs = new UserPreferences(this);
        userPrefs.saveGoogleSignIn(googleUsername, googleEmail);

        // Chuyển hướng đến MainActivity
        navigateToMainActivity();

    }
}
