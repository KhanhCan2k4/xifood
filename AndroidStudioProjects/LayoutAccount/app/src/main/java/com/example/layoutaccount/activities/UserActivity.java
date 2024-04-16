package com.example.layoutaccount.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.layoutaccount.R;
import com.example.layoutaccount.adapters.RecentsProductsAdapter;
import com.example.layoutaccount.databinding.AccountLayoutBinding;
import com.example.layoutaccount.models.Product;
import com.example.layoutaccount.models.User;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    private AccountLayoutBinding binding;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AccountLayoutBinding.inflate(
                getLayoutInflater()
        );
        setContentView(binding.getRoot());
        user = dataUser();

//        binding.imageUser.setImageResource(user.getImage());
        binding.nameUser.setText(user.getName());
        binding.bioUser.setText(user.getBio());
        binding.genderUser.setText(user.getGender());
        binding.dayBornUser.setText(user.getDayBorn());
        binding.emailUser.setText(user.getEmail());
        binding.phoneNumberUser.setText(user.getPhoneNumber());

    }

    public User dataUser(){
        User user1 = new User();
        user1.setImage("");
        user1.setName("Dylan");
        user1.setBio("2019 kết hôn với Khoai lang Thang 2022 kết hôn với Quân Ap, 2023 kết hôn với Mono");
        user1.setGender("Bisexcent");
        user1.setDayBorn("28/01/2004");
        user1.setEmail("vandupluss@gmail.com");
        user1.setPhoneNumber("085850234");
        return user1;
    }
}