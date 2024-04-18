package com.example.doan;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doan.Adapter.CartAdapter;
import com.example.doan.Adapter.ToppinAdapter;
import com.example.doan.Data.CartData;
import com.example.doan.Model.Cart;
import com.example.doan.databinding.ActivityCartLayoutBinding;
import com.example.doan.databinding.ProductDetailsLayoutBinding;

import java.util.ArrayList;

public class CartAcitvity extends AppCompatActivity {
private ActivityCartLayoutBinding binding;
private ArrayList<Cart> carts;
private CartAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        carts = CartData.getCarts();

        adapter = new CartAdapter(this, carts);
        Log.d("Cart", carts.size()+"");
        LinearLayoutManager manager =new LinearLayoutManager(CartAcitvity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.listCart.setLayoutManager(manager);
        binding.listCart.setAdapter(adapter);
//        toppinAdapter = new ToppinAdapter(this, toppings);
//        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);

    }
}