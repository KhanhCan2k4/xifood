package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myxifood.R;
import com.example.myxifood.databinding.ListProductsLayoutBinding;
import com.example.myxifood.databinding.MainLayoutBinding;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.ListCategoryAdapter;
import vn.edu.tdc.xifood.adapters.ListProductsAdapter;
import vn.edu.tdc.xifood.datas.CategoryData;
import vn.edu.tdc.xifood.datas.ListProductsData;
import vn.edu.tdc.xifood.models.Category;
import vn.edu.tdc.xifood.models.Product;

public class ListProductsActivity extends AppCompatActivity {

    private ListProductsLayoutBinding binding;
    private ArrayList<Product> products;
    private ArrayList<Category> categories;
    private ListCategoryAdapter listCategoryAdapter;
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
        binding.categoryName.setText(categories.get(id).getName());

        //san pham, doi du lieu trong danh sach san pham theo danh muc
        products = ListProductsData.getProducts();

        ListProductsAdapter adapter = new ListProductsAdapter(this, products);
        GridLayoutManager manager2 = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.HORIZONTAL);

        binding.listProducts.setLayoutManager(manager2);
        binding.listProducts.setAdapter(adapter);
    }
}