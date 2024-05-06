package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.databinding.LoginLayoutBinding;
import vn.edu.tdc.xifood.datamodels.User;
import vn.edu.tdc.xifood.staffProcessing.MainStaffActivity;

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

        UserAPI.all(new UserAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<User> users) {
                Log.d("nhi", "onCallback: " + users.size());
            }
        });

//       Nút đăng nhập
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainStaffActivity.class);
                startActivity(intent);
                finish();
            }
        });

//       Nút đăng ký
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