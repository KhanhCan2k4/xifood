package com.example.doan;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doan.Adapter.BillAdapter;
import com.example.doan.Adapter.ToppinAdapter;
import com.example.doan.Data.BillData;
import com.example.doan.Data.ToppingData;
import com.example.doan.Model.Bill;
import com.example.doan.Model.Topping;
import com.example.doan.databinding.ProductDetailsLayoutBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private ProductDetailsLayoutBinding binding;
private ArrayList<Topping> toppings;
    private ArrayList<Bill> bills;
private ToppinAdapter toppinAdapter ;
private BillAdapter billAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ProductDetailsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get data
        toppings = ToppingData.getToppings();

//        Log.d("ToppingData", "Size of toppings: " + toppings.size());
        toppinAdapter = new ToppinAdapter(this, toppings);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager manager2 = new LinearLayoutManager(MainActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);


        binding.toppingList.setLayoutManager(manager);
        binding.toppingList.setAdapter(toppinAdapter);

        //bill
        bills= BillData.getBills();
        billAdapter =new BillAdapter(this, bills);
        binding.oderListView.setLayoutManager(manager2);
        binding.oderListView.setAdapter(billAdapter);
    }
}