package vn.edu.tdc.xifood.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.activities.MainActivity;
import vn.edu.tdc.xifood.activities.RegisterActivity;
import vn.edu.tdc.xifood.data.UserPreferences;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());

        findViewById(R.id.btnRegister).setOnClickListener(v -> navigateToRegister());

        findViewById(R.id.btnSignIn).setOnClickListener(v -> login());

        findViewById(R.id.loginWithGoogle).setOnClickListener(v -> signInWithGoogle());
    }

    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void login() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            showAlert("LỖI", "Vui lòng điền đầy đủ thông tin");
            return;
        }

        // Kiểm tra thông tin đăng nhập
        UserPreferences userPrefs = new UserPreferences(this);
        if (userPrefs.checkLogin(username, password)) {
            // Đăng nhập thành công
            navigateToMainActivity();
        } else {
            // Đăng nhập thất bại
            showAlert("LỖI", "Đăng nhập không thành công. Vui lòng kiểm tra lại thông tin đăng nhập.");
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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
                showAlert("LỖI", "Đăng nhập không thành công. Vui lòng kiểm tra lại thông tin đăng nhập.");
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
                    Log.e("GoogleSignIn", "Đăng nhập không thành công", e);
                    showAlert("LỖI", "Đăng nhập không thành công");
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
