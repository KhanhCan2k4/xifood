package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.ListCategoryAdapter;
import vn.edu.tdc.xifood.adapters.ListProductsAdapter;
import vn.edu.tdc.xifood.apis.CategoryAPI;
import vn.edu.tdc.xifood.apis.ProductAPI;
import vn.edu.tdc.xifood.data.CategoryData;
import vn.edu.tdc.xifood.data.ListProductsData;
import vn.edu.tdc.xifood.databinding.ListProductsLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Category;
import vn.edu.tdc.xifood.datamodels.Product;
import vn.edu.tdc.xifood.views.Navbar;

public class ListProductsActivity extends AppCompatActivity {

    private ListProductsLayoutBinding binding;
    private ArrayList<Product> products;
    private ArrayList<Category> categories;
    private ListCategoryAdapter listCategoryAdapter;
    private ListProductsAdapter adapter;
    private int id;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //khoi tao
        binding = ListProductsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //lay du lieu
//        categories = CategoryData.getCategoryArrayList();
        categories = new ArrayList<>();
        key = getIntent().getStringExtra(MainActivity.CLICKED_CATEGORY_KEY);
        Log.d("CATE", "onCreate: " + key);
        CategoryAPI.all(new CategoryAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Category> categoriesList) {
                for (Category category:categoriesList) {
                    categories.add(category);
                }
                listCategoryAdapter = new ListCategoryAdapter(ListProductsActivity.this, categories);
                LinearLayoutManager manager = new LinearLayoutManager(ListProductsActivity.this);

                //nhan gia tri
//        id = new Intent().getIntExtra("id", id);
//        id = 2;
                id = getIntent().getIntExtra("id", id);

                // xet huong
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);

                binding.listCategory.setLayoutManager(manager);
                binding.listCategory.setAdapter(listCategoryAdapter);

                //goi uy quyen cho danh muc
                listCategoryAdapter.setItemClick(new ListCategoryAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(ListCategoryAdapter.ViewHolder holder) {
                        String nextKey = holder.getCategoryKey();
                        if (nextKey != key) {
                            if (!nextKey.isEmpty()) {
                                upDate(nextKey);
                            } else {
                                //chuyen ve main (khong can truyen du lieu)
                                Intent intent = new Intent(ListProductsActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                                // chuyen
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
        CategoryAPI.find(key, new CategoryAPI.FirebaseCallback() {
            @Override
            public void onCallback(Category category) {
                binding.categoryName.setText(category.getName());

                products = category.getProducts();
                adapter = new ListProductsAdapter(ListProductsActivity.this, products);
                GridLayoutManager manager2 = new GridLayoutManager(ListProductsActivity.this, 3);
                manager2.setOrientation(GridLayoutManager.VERTICAL);

                binding.listProducts.setLayoutManager(manager2);
                binding.listProducts.setAdapter(adapter);
            }
        });


        binding.navbar.setNavClickListener(new Navbar.OnNavClickListener() {
            @Override
            public void onHomeButtonClick(View view) {
                //ignore
                Intent intent = new Intent(ListProductsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onDiscountButtonClick(View view) {
                //ignore
            }

            @Override
            public void onOrderButtonClick(View view) {
                Intent intent = new Intent(ListProductsActivity.this, OrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onAccountButtonClick(View view) {
                Intent intent = new Intent(ListProductsActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }
        });
    }

    private void upDate(String nextKey) {
        key = nextKey;
        for (Category category: categories) {
            if (category.getKey().equals(key)) {
                binding.categoryName.setText(category.getName());
                products = category.getProducts();
                adapter.setProducts(products);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        key = getIntent().getStringExtra(MainActivity.CLICKED_CATEGORY_KEY);
        upDate(key);
    }
}