package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.ListSearchProductsAdapter;
import vn.edu.tdc.xifood.adapters.ItemSearchAdapter;
import vn.edu.tdc.xifood.databinding.ListSearchLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Product;

public class ListSearchActivity extends AppCompatActivity {

    private ListSearchLayoutBinding binding;
    private ArrayList<Product> products;
    private ItemSearchAdapter itemSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ListSearchLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<Product>();

        // gan adapter cho recycleview
        // khoi tao adapter
        itemSearchAdapter = new ItemSearchAdapter(products, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);

        binding.listSearchProducts.setLayoutManager(layoutManager);
        binding.listSearchProducts.setAdapter(itemSearchAdapter);

        // chuyen lai mang hinh truoc do
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        itemSearchAdapter.setItemClickListener(new ItemSearchAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ListSearchProductsAdapter.ViewHolder holder) {
                Log.d("99", "onItemClick: hihi");
            }
        });
    }
}