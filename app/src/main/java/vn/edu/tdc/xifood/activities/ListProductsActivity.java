package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.ListCategoryAdapter;
import vn.edu.tdc.xifood.adapters.ListProductsAdapter;
import vn.edu.tdc.xifood.data.CategoryData;
import vn.edu.tdc.xifood.data.ListProductsData;
import vn.edu.tdc.xifood.databinding.ListProductsLayoutBinding;
import vn.edu.tdc.xifood.models.Category;
import vn.edu.tdc.xifood.models.Product;

public class ListProductsActivity extends AppCompatActivity {

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
        categories = CategoryData.getCategoryArrayList();
        listCategoryAdapter = new ListCategoryAdapter(this, categories);
        LinearLayoutManager manager = new LinearLayoutManager(ListProductsActivity.this);

        //nhan gia tri
//        id = new Intent().getIntExtra("id", id);
//        id = 2;
        id = getIntent().getIntExtra("id", id);
        Log.d("TAG", "onCreate: " + id);

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
                        //chuyen ve main (khong can truyen du lieu)
                        Intent intent = new Intent(ListProductsActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                        // chuyen
                        startActivity(intent);
                    }
                }
            }
        });

        //san pham, doi du lieu trong danh sach san pham theo danh muc
        products = ListProductsData.getProducts();

        adapter = new ListProductsAdapter(this, products);
        GridLayoutManager manager2 = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.HORIZONTAL);

        binding.listProducts.setLayoutManager(manager2);
        binding.listProducts.setAdapter(adapter);

    }

    private void upDate(int nextId) {
        id = nextId;

        binding.categoryName.setText(categories.get(id).getName());
        products = ListProductsData.getProducts();

        adapter.notifyDataSetChanged();
    }
}