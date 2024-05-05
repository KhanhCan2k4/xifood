package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.Product;
import vn.edu.tdc.xifood.adapters.RecentsProductsAdapter;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.databinding.SettingLayoutBinding;
import vn.edu.tdc.xifood.datamodels.User;
import vn.edu.tdc.xifood.views.Navbar;

public class SettingActivity extends AppCompatActivity {
    private SettingLayoutBinding binding;
    RecentsProductsAdapter adapter;
    private ArrayList<Product> products = new ArrayList<>();

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //lay nguoi dung tu SharePreference
        SharePreference.setSharedPreferences(SettingActivity.this); // phai co moi chay nhen ong co
        String key = "";
        key = SharePreference.find("USER_ID");
        key = "0";


        if (!key.isEmpty()) {
            UserAPI.find(key, new UserAPI.FirebaseCallback() {
                // lay user co key
                @Override
                public void onCallback(User user) {
//                    Log.d("TAG", "onCallback: " + user.getFullName());
                    binding.username.setText(user.getFullName());
                }
            });

            // lay anh tu ImageStoragePreference
            ImageStorageReference.setImageInto(binding.imageAvatar, "avatars/a2.jpg");

        }

        products = dataProduct();

        adapter = new RecentsProductsAdapter(this, products);

        Log.d("product", products.size() + "");

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

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    public ArrayList<Product> dataProduct() {
        Product product1 = new Product();
        product1.setImageProduct("");
        product1.setNameProduct("Cà phê sữa tươi");
        product1.setPriceProduct(59000);
        products.add(product1);

        Product product2 = new Product();
        product2.setImageProduct("");
        product2.setNameProduct("Sữa tươi");
        product2.setPriceProduct(69000);
        products.add(product2);

        Product product3 = new Product();
        product3.setImageProduct("");
        product3.setNameProduct("Trà sữa");
        product3.setPriceProduct(45000);
        products.add(product3);

        Product product4 = new Product();
        product4.setImageProduct("");
        product4.setNameProduct("Lipton");
        product4.setPriceProduct(42000);
        products.add(product4);

        return products;
    }
}