package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.databinding.LoginLayoutBinding;

public class LoginActivity extends AppCompatActivity {
    private LoginLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LoginLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.username.getText().toString().trim();
                String password = binding.password.getText().toString().trim();

                if (username == "admin" && password == "111") {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Unsuccessfully", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}