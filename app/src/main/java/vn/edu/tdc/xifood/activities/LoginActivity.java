package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import vn.edu.tdc.xifood.databinding.LoginLayoutBinding;

public class LoginActivity extends AppCompatActivity {
    private LoginLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        String a = "abcxyz";
//
//        SharePreference.setSharedPreferences(this);
//        SharePreference.store("1", a);
//        Log.d("TAG", "SharePreference: " + SharePreference.find("1"));

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

    }
}