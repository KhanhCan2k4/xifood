package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.adapters.ListProductsAdapter;
import vn.edu.tdc.xifood.adapters.ItemSearchAdapter;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.ListSearchLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.Product;

public class ListSearchActivity extends AppCompatActivity {

    private ListSearchLayoutBinding binding;
    private ArrayList<Product> productsSearch;
    private ItemSearchAdapter itemSearchAdapter;
    private String keyWord = "";
    private ListProductsAdapter listProductsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //declare binding
        super.onCreate(savedInstanceState);
        binding = ListSearchLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //declare filter
        binding.openFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                binding.filter.setVisibility(b ? View.VISIBLE : View.GONE);
                search();
            }
        });

        EditText editText = binding.keyWordOfSearchLayout;
        productsSearch = new ArrayList<Product>();

        itemSearchAdapter = new ItemSearchAdapter(productsSearch, ListSearchActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListSearchActivity.this);
        layoutManager.setReverseLayout(false);
        binding.listSearchProducts.setLayoutManager(layoutManager);
        binding.listSearchProducts.setAdapter(itemSearchAdapter);

        listProductsAdapter = new ListProductsAdapter(ListSearchActivity.this, new ArrayList<>());
        GridLayoutManager layoutManager1 = new GridLayoutManager(ListSearchActivity.this, 1);
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
                for (String key : arrKeys) {
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
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                keyWord = String.valueOf(binding.keyWordOfSearchLayout.getText());
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

        //get all product
        API<Product> productAPI = new API<>(Product.class, API.PRODUCT_TABLE_NAME);
        productAPI.all(new API.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList list) {
                productsSearch = list;
                search();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Product> searchedProducts = new ArrayList<Product>();

        String[] keys = SharePreference.find(SharePreference.LIKED_PRODUCTS_KEY).split("_____");
        for (String key : keys) {
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

    public void search() {
        ArrayList<Product> searchedProducts = new ArrayList<Product>();
        for (int i = 0; i < productsSearch.size(); i++) {
            boolean filter = keyWord.isEmpty();

            if (!binding.openFilter.isChecked()) {  //filter all
                filter = filter || productsSearch.get(i).getName().toLowerCase().contains(keyWord.toLowerCase());
            } else { // filter with its attributes
                filter = true;
                boolean flag = false;
                if (binding.ckbName.isChecked()) {
                    filter = filter && productsSearch.get(i).getName().toLowerCase().contains(keyWord.toLowerCase());
//                    Log.d("TAG", "search: filter name: " + filter);
                    flag = true;
                }

                if (binding.ckbDesc.isChecked()) {
                    filter = filter && productsSearch.get(i).getDescription().toLowerCase().contains(keyWord.toLowerCase());
//                    Log.d("TAG", "search: filter desc: " + filter);
                    flag = true;
                }

                int ratingValue = binding.ckbRating.getSelectedItemPosition();
                if (ratingValue > 0) {
                    filter = filter && productsSearch.get(i).getRating() >= ratingValue;
//                    Log.d("TAG", "search: filter rating: " + filter);
                    flag = true;
                }

                float minPrice;
                try {
                    minPrice = Float.parseFloat(binding.minPrice.getText().toString());
                } catch (Exception e) {
                    minPrice = 0.0f;
                }

                float maxPrice;
                try {
                    maxPrice = Float.parseFloat(binding.maxPrice.getText().toString());
                } catch (Exception e) {
                    maxPrice = 0.0f;
                }

                if (minPrice < maxPrice) {
                    filter = filter && productsSearch.get(i).getPrice() >= minPrice && productsSearch.get(i).getPrice() <= maxPrice;
//                    Log.d("TAG", "search: filter price: " + filter);
                    flag = true;
                }

                filter = filter && flag;
            }

//            Log.d("TAG", "search: filter" + filter);
            if (filter) {
                searchedProducts.add(productsSearch.get(i));
            }
        }

        itemSearchAdapter.setProducts(searchedProducts);
        itemSearchAdapter.notifyDataSetChanged();
    }
}