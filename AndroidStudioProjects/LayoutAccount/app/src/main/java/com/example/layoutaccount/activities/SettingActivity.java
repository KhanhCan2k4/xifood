package com.example.layoutaccount.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.layoutaccount.adapters.RecentsProductsAdapter;
import com.example.layoutaccount.databinding.SettingLayoutBinding;
import com.example.layoutaccount.models.Product;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    private SettingLayoutBinding binding;
    RecentsProductsAdapter adapter;
    private ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingLayoutBinding.inflate(
                getLayoutInflater()
        );
        setContentView(binding.getRoot());
        products = dataProduct();

        adapter = new RecentsProductsAdapter(this, products);

        Log.d("product", products.size()+ "");

        //Tạo đối tượng layout Mânger
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recentsSettingRecycleView.setLayoutManager(layoutManager);

        binding.recentsSettingRecycleView.setAdapter(adapter);
    }
    public ArrayList<Product> dataProduct(){
        Product product1 = new Product();
        product1.setImageProduct("");
        product1.setNameProduct("Cà phê sữa tươi");
        product1.setPriceProduct(59000);
        products.add(product1);

        Product product2 = new Product();
        product2.setImageProduct("");
        product2.setNameProduct("Sữa tươi");
        product2.setPriceProduct(69000);
        products.add(product2);

        Product product3 = new Product();
        product3.setImageProduct("");
        product3.setNameProduct("Trà sữa");
        product3.setPriceProduct(45000);
        products.add(product3);

        Product product4 = new Product();
        product4.setImageProduct("");
        product4.setNameProduct("Lipton");
        product4.setPriceProduct(42000);
        products.add(product4);

        return  products;
    }
}