package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.ListCategoryAdapter;
import vn.edu.tdc.xifood.adapters.ListProductsAdapter;
import vn.edu.tdc.xifood.apis.CategoryAPI;
import vn.edu.tdc.xifood.apis.ConfigAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.databinding.MainLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Category;
import vn.edu.tdc.xifood.datamodels.Product;
import vn.edu.tdc.xifood.views.Navbar;

public class MainActivity extends AppCompatActivity {
    private MainLayoutBinding binding;
    private ArrayList<Product> products;
    private ArrayList<Category> categories;
    private ListCategoryAdapter listCategoryAdapter;
    private ViewFlipper viewFlipper;
    public static final String CLICKED_CATEGORY_KEY = "CLICKED_CATEGORY_KEY";
    public static final String NEW_PRODUCT_KEY = "NEW_PRODUCT_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //khoi tao
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //category adapter
        categories = new ArrayList<>();
        listCategoryAdapter = new ListCategoryAdapter(MainActivity.this, categories);
        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 5);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listCategory.setLayoutManager(manager);
        binding.listCategory.setAdapter(listCategoryAdapter);
        //goi uy quyen cho danh muc
        listCategoryAdapter.setItemClick(new ListCategoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ListCategoryAdapter.ViewHolder holder) {
                String key = holder.getCategoryKey();
                if (!key.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, ListProductsActivity.class);
                    intent.putExtra(CLICKED_CATEGORY_KEY, key);
                    Log.d(CLICKED_CATEGORY_KEY, key + "");

                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    // chuyen
                    startActivity(intent);
                }
            }
        });

        //product adapter
        products = new ArrayList<>();
        ListProductsAdapter adapter = new ListProductsAdapter(MainActivity.this, products);
        GridLayoutManager manager2 = new GridLayoutManager(MainActivity.this, 3);
        manager2.setOrientation(GridLayoutManager.VERTICAL);

        binding.listProducts.setLayoutManager(manager2);
        binding.listProducts.setAdapter(adapter);
        adapter.setItemClickListener(new ListProductsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ListProductsAdapter.ViewHolder holder) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.DETAIL_PRODUCT_KEY, holder.getProductKey());

                startActivity(intent);
            }
        });

        //lay du lieu
        CategoryAPI.all(new CategoryAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Category> categoriesList) {
               categories = categoriesList;
               listCategoryAdapter.setCategories(categories);
               listCategoryAdapter.notifyDataSetChanged();

                for (Category category: categories ) {
                    if (category.getKey().equals(NEW_PRODUCT_KEY)) {
                        products = category.getProducts();
                        adapter.setProducts(products);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });

        viewFlipper = findViewById(R.id.bannerViewFlipper);
        ActionViewFlipper();

        binding.navbar.setActiveIndex(0);
        binding.navbar.setNavClickListener(new Navbar.OnNavClickListener() {
            @Override
            public void onHomeButtonClick(View view) {
                //ignore
            }

            @Override
            public void onDiscountButtonClick(View view) {
                //chuyen qua danh muc uu dai
                Intent intent = new Intent(MainActivity.this, ListProductsActivity.class);
                intent.putExtra(MainActivity.CLICKED_CATEGORY_KEY, ListProductsActivity.DISCOUNT_KEY);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onOrderButtonClick(View view) {
                Intent intent;
                intent = new Intent(MainActivity.this, OrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                // chuyen
                startActivity(intent);
            }

            @Override
            public void onAccountButtonClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }
        });

        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;

                if (SharePreference.findPermission() == UserAPI.STAFF_PERMISSION) {
                    intent = new Intent(MainActivity.this, StaffOrderActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, CartActivity.class);
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        // chuyen sang ListSearchActivity
        binding.txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListSearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //set app's info
        ConfigAPI.find("APP_EVENT", new ConfigAPI.FirebaseCallback() {
            @Override
            public void onCallback(String config) {
                binding.event.setText(config);
            }
        });

        ConfigAPI.find("APP_EVENT_DESC", new ConfigAPI.FirebaseCallback() {
            @Override
            public void onCallback(String config) {
                binding.eventDesc.setText(config);
            }
        });

        binding.websiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigAPI.find("APP_LINK", new ConfigAPI.FirebaseCallback() {
                    @Override
                    public void onCallback(String config) {
//                Log.d("--LINK", "onCallback: " + config);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(config));

                        startActivity(intent);
                    }
                });
            }
        });
    }
    // gọi action để chạy ViewFlipper
    private void ActionViewFlipper() {
        List<String> bannerViewFlipper = new ArrayList<>();
        bannerViewFlipper.add("https://th.bing.com/th?id=OIP.0QBVEpjVjCxaioYJQBZHOgHaDk&w=350&h=168&c=8&rs=1&qlt=90&o=6&dpr=1.3&pid=3.1&rm=2");
        bannerViewFlipper.add("https://th.bing.com/th?id=OIP.82a09uRdBq-0iIvzxpVT3QHaDL&w=350&h=150&c=8&rs=1&qlt=90&o=6&dpr=1.3&pid=3.1&rm=2");
        bannerViewFlipper.add("https://th.bing.com/th?id=OIP.2J5DAk7KhVokihYW1S4w2gHaDL&w=350&h=150&c=8&rs=1&qlt=90&o=6&dpr=1.3&pid=3.1&rm=2");

        for (int i = 0; i < bannerViewFlipper.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(bannerViewFlipper.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }
}