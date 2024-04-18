package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.data.AccountData;
import vn.edu.tdc.xifood.databinding.LoginLayoutBinding;
import vn.edu.tdc.xifood.models.Account;
import vn.edu.tdc.xifood.models.User;

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
                ArrayList<Account> listAccounts = AccountData.dataAccount();
                //lay account co san
                for (Account acc: listAccounts) {
                    Account loginAccount = acc;

                    //lay du lieu tu nguoi dung nhap vao
                    String username = binding.username.getText().toString().trim();
                    String password = binding.password.getText().toString().trim();

                    //Nhan du lieu từ Register Activity
//                    Intent intentData = getIntent();
//                    String value1 = intentData.getStringExtra("Array_Account");
//                    Log.d("si", value1);

                    if (loginAccount.getUsername().equalsIgnoreCase(username) && loginAccount.getPassword().equalsIgnoreCase(password)) {
                        //Dang nhap hop le
                        if (loginAccount.getRole() == 0) {
                            //Khach hang
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //Admin
                            //...//
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Unsuccessfully", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


}