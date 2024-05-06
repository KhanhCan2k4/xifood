package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
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
import vn.edu.tdc.xifood.data.CategoryData;
import vn.edu.tdc.xifood.data.ListProductsData;
import vn.edu.tdc.xifood.databinding.MainLayoutBinding;
import vn.edu.tdc.xifood.models.Category;
import vn.edu.tdc.xifood.models.Product;
import vn.edu.tdc.xifood.views.Navbar;

public class MainActivity extends AppCompatActivity {
    private MainLayoutBinding binding;
    private ArrayList<Product> products;
    private ArrayList<Category> categories;
    private ListCategoryAdapter listCategoryAdapter;
    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //khoi tao
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //lay du lieu
        categories = CategoryData.getCategoryArrayList();
        listCategoryAdapter = new ListCategoryAdapter(this, categories);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);

        // xet huong
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        binding.listCategory.setLayoutManager(manager);
        binding.listCategory.setAdapter(listCategoryAdapter);

        //san pham
//        products = ListProductsData.getProducts();
        products = CategoryData.getProductsByCategoryID(5);

        ListProductsAdapter adapter = new ListProductsAdapter(this, products);
        GridLayoutManager manager2 = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.HORIZONTAL);

        binding.listProducts.setLayoutManager(manager2);
        binding.listProducts.setAdapter(adapter);


        //goi uy quyen cho danh muc
        listCategoryAdapter.setItemClick(new ListCategoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ListCategoryAdapter.ViewHolder holder) {
                int id = holder.getId();
                if (id > 0) {
                    Intent intent = new Intent(MainActivity.this, ListProductsActivity.class);
                    intent.putExtra("id", id);
                    Log.d("id", id + "");

                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    // chuyen
                    startActivity(intent);
                }
            }
        });

        viewFlipper = findViewById(R.id.bannerViewFlipper);
        ActionViewFlipper();
        //viết sự kiện cho các button trong navbar
        binding.navbar.setNavClickListener(new Navbar.OnNavClickListener() {
            @Override
            public void onHomeButtonClick(View view) {
                //ignore
            }

            @Override
            public void onDiscountButtonClick(View view) {
                //chuyen qua danh muc uu dai
                Intent intent = new Intent(MainActivity.this, ListProductsActivity.class);
                intent.putExtra("id", 1);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onOrderButtonClick(View view) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
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
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        adapter.setItemClickListener(new ListProductsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ListProductsAdapter.ViewHolder holder) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", holder.getProductId());

                startActivity(intent);
            }
        });

    }

    // gọi action để chạy ViewFlipper
    private void ActionViewFlipper(){
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