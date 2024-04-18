package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.ListProductsAdapter;
import vn.edu.tdc.xifood.data.CategoryData;
import vn.edu.tdc.xifood.databinding.SettingLayoutBinding;
import vn.edu.tdc.xifood.views.Navbar;

public class SettingActivity extends AppCompatActivity {
    private SettingLayoutBinding binding;
    ListProductsAdapter adapter;
    private ArrayList<vn.edu.tdc.xifood.models.Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        products = dataProduct();
            products = CategoryData.getProductsByCategoryID(6);
        adapter = new ListProductsAdapter(this, products);

        Log.d("product", products.size()+ "");

        //Tạo đối tượng layout Mânger
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recentsSettingRecycleView.setLayoutManager(layoutManager);

        binding.recentsSettingRecycleView.setAdapter(adapter);

        binding.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, AccountActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                // chuyen
                startActivity(intent);
            }
        });

        binding.navbar.setNavClickListener(new Navbar.OnNavClickListener() {
            @Override
            public void onHomeButtonClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onDiscountButtonClick(View view) {
                //chuyen qua danh muc uu dai
                Intent intent = new Intent(SettingActivity.this, ListProductsActivity.class);
                intent.putExtra("id", 1);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onOrderButtonClick(View view) {
                Intent intent = new Intent(SettingActivity.this, OrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onAccountButtonClick(View view) {
               //ignore
            }
        });

    }
//    public ArrayList<Product> dataProduct(){
//        Product product1 = new Product();
//        product1.setImageProduct("");
//        product1.setNameProduct("Cà phê sữa tươi");
//        product1.setPriceProduct(59000);
//        products.add(product1);
//
//        Product product2 = new Product();
//        product2.setImageProduct("");
//        product2.setNameProduct("Sữa tươi");
//        product2.setPriceProduct(69000);
//        products.add(product2);
//
//        Product product3 = new Product();
//        product3.setImageProduct("");
//        product3.setNameProduct("Trà sữa");
//        product3.setPriceProduct(45000);
//        products.add(product3);
//
//        Product product4 = new Product();
//        product4.setImageProduct("");
//        product4.setNameProduct("Lipton");
//        product4.setPriceProduct(42000);
//        products.add(product4);
//
//        return  products;
//    }
}