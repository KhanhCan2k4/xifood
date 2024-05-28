package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.ListProductsAdapter;
import vn.edu.tdc.xifood.adapters.ItemSearchAdapter;
import vn.edu.tdc.xifood.apis.ProductAPI;
import vn.edu.tdc.xifood.apis.ProductAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.databinding.ListSearchLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Product;

public class ListSearchActivity extends AppCompatActivity {

    private ListSearchLayoutBinding binding;
    private ArrayList<Product> productsSearch;
    private ItemSearchAdapter itemSearchAdapter;
    private String keyWord = "";
    private ListProductsAdapter listProductsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ListSearchLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EditText editText = binding.keyWordOfSearchLayout;
        productsSearch = new ArrayList<Product>();

        itemSearchAdapter = new ItemSearchAdapter(productsSearch, ListSearchActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListSearchActivity.this);
        layoutManager.setReverseLayout(false);
        binding.listSearchProducts.setLayoutManager(layoutManager);
        binding.listSearchProducts.setAdapter(itemSearchAdapter);

        listProductsAdapter = new ListProductsAdapter(ListSearchActivity.this, new ArrayList<>());
        GridLayoutManager layoutManager1 = new GridLayoutManager(ListSearchActivity.this, 3);
        layoutManager1.setOrientation(GridLayoutManager.VERTICAL);
        binding.recentSearchList.setLayoutManager(layoutManager1);
        binding.recentSearchList.setAdapter(listProductsAdapter);

        itemSearchAdapter.setItemClickListener(new ItemSearchAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ItemSearchAdapter.MyViewHolder holder) {
                Intent intent = new Intent(ListSearchActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.DETAIL_PRODUCT_KEY, holder.getKey());

                String txtKeys = SharePreference.find(SharePreference.LIKED_PRODUCTS_KEY);
                String[] keys = txtKeys.split("_____");
                ArrayList<String> arrKeys = new ArrayList<>();

                for (String key : keys) {
                    if (key.equals(holder.getKey())) { //exists
                        arrKeys.remove(key); //remove
                    } else {
                        arrKeys.add(key + "_____");
                    }
                }

                if (arrKeys.size() < 6) {
                    arrKeys.add(0, holder.getKey() + "_____");
                } else {
                    arrKeys.remove(5);
                    arrKeys.add(0, holder.getKey() + "_____");
                }

                txtKeys = "";
                for (String key: arrKeys ) {
                    txtKeys += key;
                }
                SharePreference.store(
                        SharePreference.LIKED_PRODUCTS_KEY, //overwrites the previous value
                       txtKeys //shave key
                );

                startActivity(intent);
            }
        });

        listProductsAdapter.setItemClickListener(new ListProductsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ListProductsAdapter.ViewHolder holder) {
                Intent intent = new Intent(ListSearchActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.DETAIL_PRODUCT_KEY, holder.getProductKey());
                startActivity(intent);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search("");
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

        // chuyen lai man hinh truoc do
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });


        ProductAPI.all(new ProductAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Product> products) {
                productsSearch = products;
                search("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.d("--TAG", "onResume: " + SharePreference.find(SharePreference.LIKED_PRODUCTS_KEY));
        ArrayList<Product> searchedProducts = new ArrayList<Product>();

        String[] keys = SharePreference.find(SharePreference.LIKED_PRODUCTS_KEY).split("_____");
//        Log.d("--TAG", "keys size : " + keys.length);
        for (String key : keys) {
//            Log.d("--TAG", "key: " + key);
            if (key.isEmpty()) continue;
            for (Product product : productsSearch) {
                if (product.getKey().equals(key)) {
                    searchedProducts.add(product);
                }
            }
        }

        listProductsAdapter.setProducts(searchedProducts);
        listProductsAdapter.notifyDataSetChanged();
    }

    public void search(String key) {
        ArrayList<Product> searchedProducts = new ArrayList<Product>();
        for (int i = 0; i < productsSearch.size(); i++) {
            if (key.isEmpty() || productsSearch.get(i).getName().toLowerCase().contains(key.toLowerCase()) == true) {
                searchedProducts.add(productsSearch.get(i));
            }
        }

        itemSearchAdapter.setProducts(searchedProducts);
        itemSearchAdapter.notifyDataSetChanged();
    }
}