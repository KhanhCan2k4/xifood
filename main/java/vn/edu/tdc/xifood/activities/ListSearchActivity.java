package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.ListSearchProductsAdapter;
import vn.edu.tdc.xifood.adapters.ItemSearchAdapter;
import vn.edu.tdc.xifood.apis.ProductAPI;
import vn.edu.tdc.xifood.databinding.ListSearchLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Product;

public class ListSearchActivity extends AppCompatActivity {

    private ListSearchLayoutBinding binding;
    private ArrayList<Product> productsSearch;
    private ItemSearchAdapter itemSearchAdapter;
    private String keyWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ListSearchLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageButton startSearch = binding.startSearch;
        EditText editText = binding.keyWordOfSearchLayout;

        productsSearch = new ArrayList<Product>();
        ProductAPI.all(new ProductAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Product> products) {
                productsSearch=products;
                itemSearchAdapter = new ItemSearchAdapter(productsSearch, ListSearchActivity.this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ListSearchActivity.this);
                layoutManager.setReverseLayout(false);
                binding.listSearchProducts.setLayoutManager(layoutManager);
                binding.listSearchProducts.setAdapter(itemSearchAdapter);
            }
        });



        startSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyWord = String.valueOf(binding.keyWordOfSearchLayout.getText());
                Log.d("startSearch", "onItemClick: " + keyWord);

//                productsSearch = search(keyWord);
//                itemSearchAdapter = new ItemSearchAdapter(productsSearch, ListSearchActivity.this);
//                LinearLayoutManager layoutManager = new LinearLayoutManager(ListSearchActivity.this);
//                layoutManager.setReverseLayout(false);
//                binding.listSearchProducts.setLayoutManager(layoutManager);
//                binding.listSearchProducts.setAdapter(itemSearchAdapter);
                ProductAPI.all(new ProductAPI.FirebaseCallbackAll() {
                    @Override
                    public void onCallback(ArrayList<Product> products) {
                        productsSearch.clear();
                        for (int i = 0; i < products.size(); i++) {

                            if (products.get(i).getName().toLowerCase().contains(keyWord.toLowerCase()) == true) {
//                                productsSearch.clear();
                                productsSearch.add(products.get(i));
                                Log.d("product search list", "product " + products.get(i).getName());
                            }
                        }
                        Log.d("a", "onTextChanged: " + productsSearch.size());
                        itemSearchAdapter.notifyDataSetChanged();
                    }
                });


            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                ProductAPI.all(new ProductAPI.FirebaseCallbackAll() {
                    @Override
                    public void onCallback(ArrayList<Product> products) {
                        for (int i = 0; i < products.size(); i++) {
                            productsSearch.add(products.get(i));
                        }
                        itemSearchAdapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                keyWord = String.valueOf(binding.keyWordOfSearchLayout.getText());
                search(keyWord);
            }

            @Override
            public void afterTextChanged(Editable s) {
                keyWord = String.valueOf(binding.keyWordOfSearchLayout.getText());
                search(keyWord);
            }
        });
        // chuyen lai mang hinh truoc do
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });


    }

    public void search(String key) {
        ProductAPI.all(new ProductAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Product> products) {
                productsSearch.clear();
                for (int i = 0; i < products.size(); i++) {
                    Log.d("dss", "onCallback: "+products.size());
                    if (products.get(i).getName().toLowerCase().contains(keyWord.toLowerCase()) == true) {
//                                productsSearch.clear();
                        productsSearch.add(products.get(i));

                        Log.d("product search list", "product " + products.get(i).getName());
                    }
                }

                Log.d("TAG", "onCallback: " + productsSearch.size());
                itemSearchAdapter.setProducts(productsSearch);
                itemSearchAdapter.notifyDataSetChanged();

            }
        });
    }

}