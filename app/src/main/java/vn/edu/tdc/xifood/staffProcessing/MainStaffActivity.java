package vn.edu.tdc.xifood.staffProcessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.activities.DetailActivity;
import vn.edu.tdc.xifood.activities.ListProductsActivity;
import vn.edu.tdc.xifood.activities.MainActivity;
import vn.edu.tdc.xifood.activities.OrderActivity;
import vn.edu.tdc.xifood.activities.SettingActivity;
import vn.edu.tdc.xifood.adapters.ListCategoryAdapter;
import vn.edu.tdc.xifood.adapters.ListProductsAdapter;
import vn.edu.tdc.xifood.data.CategoryData;
import vn.edu.tdc.xifood.databinding.ListProductsLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Category;
import vn.edu.tdc.xifood.datamodels.Product;
import vn.edu.tdc.xifood.views.Navbar;

public class MainStaffActivity extends AppCompatActivity {

    private ListProductsLayoutBinding binding;
    private ArrayList<Product> products;
    private ArrayList<Category> categories;
    private ListCategoryAdapter listCategoryAdapter;
    private ListProductsAdapter adapter;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //khoi tao
        binding = ListProductsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //lay du lieu
        categories = new ArrayList<>();
        listCategoryAdapter = new ListCategoryAdapter(this, categories);
        LinearLayoutManager manager = new LinearLayoutManager(MainStaffActivity.this);

        //nhan gia tri
//        id = new Intent().getIntExtra("id", id);
        id = 1;
//        id = getIntent().getIntExtra("id", id);

        // xet huong
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        binding.listCategory.setLayoutManager(manager);
        binding.listCategory.setAdapter(listCategoryAdapter);

        //goi uy quyen cho danh muc
        listCategoryAdapter.setItemClick(new ListCategoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ListCategoryAdapter.ViewHolder holder) {
                int nextId = holder.getId();
                if (nextId != id) {
                    if (nextId > 0) {
                        upDate(nextId);
                    } else {
                        upDate(1);
                    }
                }
            }
        });

        //san pham, doi du lieu trong danh sach san pham theo danh muc
//        products = ListProductsData.getProducts();
        products = new ArrayList<>();

        adapter = new ListProductsAdapter(this, products);
        GridLayoutManager manager2 = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.HORIZONTAL);

        binding.listProducts.setLayoutManager(manager2);
        binding.listProducts.setAdapter(adapter);

        //thay đổi nút home-icon thành nút cart
        binding.homeIcon.setBackgroundResource(R.drawable.cart_icon);
        binding.homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainStaffActivity.this, CartStaffActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//              chuyển trang
                startActivity(intent);
            }
        });

        binding.navbar.setNavClickListener(new Navbar.OnNavClickListener() {
            @Override
            public void onHomeButtonClick(View view) {
                //ignore
            }

            @Override
            public void onDiscountButtonClick(View view) {
                //ignore
            }

            @Override
            public void onOrderButtonClick(View view) {
                Intent intent = new Intent(MainStaffActivity.this, OrderStaffActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onAccountButtonClick(View view) {
                Intent intent = new Intent(MainStaffActivity.this, AccountStaffActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }
        });

        adapter.setItemClickListener(new ListProductsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ListProductsAdapter.ViewHolder holder) {
                Intent intent = new Intent(MainStaffActivity.this, DetailActivity.class);
                intent.putExtra("id", holder.getProductId());

                startActivity(intent);
            }
        });
    }

    private void upDate(int nextId) {
        id = nextId;

        binding.categoryName.setText(categories.get(id).getName());
//        products = ListProductsData.getProducts();
        products = new ArrayList<>();

        //xet mang moi, truoc khi cap nhat
        adapter.setProducts(products);
        //thong bao cap nhat
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        id = getIntent().getIntExtra("id", id);
        upDate(id);
    }
}