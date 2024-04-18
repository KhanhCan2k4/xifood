package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.data.AccountData;
import vn.edu.tdc.xifood.databinding.RegisterLayoutBinding;
import vn.edu.tdc.xifood.models.Account;

public class RegisterActivity extends AppCompatActivity {
    private RegisterLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegisterLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Account> listAccounts = AccountData.dataAccount();

                //bien kiem tra viec dang ki
                boolean isRegistered = false;

                String username = binding.username.getText().toString().trim();
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                String passwordConfirm = binding.conPass.getText().toString().trim();

                for (Account acc : listAccounts) {
                    if (acc.getUsername().equalsIgnoreCase(username) || !password.equals(passwordConfirm)) {
                        //neu tk da co nguoi dang ki || mạt khau nhập lại không chính xác
                        isRegistered = true; // đánh dấu
                        break;
                    }
                }

                if (isRegistered) {
                    //Nếu đã đăng ký
                    Toast.makeText(RegisterActivity.this, "Register Unsuccessfully", Toast.LENGTH_LONG).show();
                } else {
                    //Nếu chưa đăng ký thì thêm vào list account và chuyển qua trang Main
                    Account registerAccount = new Account(username, email, password, 0);
                    listAccounts.add(registerAccount);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}