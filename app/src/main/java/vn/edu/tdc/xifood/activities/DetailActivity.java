package vn.edu.tdc.xifood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import vn.edu.tdc.xifood.adapters.BillAdapter;
import vn.edu.tdc.xifood.adapters.ToppinAdapter;
import vn.edu.tdc.xifood.apis.ProductAPI;
import vn.edu.tdc.xifood.apis.ToppingAPI;
import vn.edu.tdc.xifood.data.BillData;
import vn.edu.tdc.xifood.data.ToppingData;
import vn.edu.tdc.xifood.databinding.ProductDetailsLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Product;
import vn.edu.tdc.xifood.models.Bill;
import vn.edu.tdc.xifood.models.Topping;
import vn.edu.tdc.xifood.views.CancelHeader;

public class DetailActivity extends AppCompatActivity {

    private ProductDetailsLayoutBinding binding;
    private ArrayList<Topping> toppings;
    private ArrayList<Bill> bills;
    private ToppinAdapter toppinAdapter;
    private BillAdapter billAdapter;
    private int soluong = 0;
    private Product product;
    private int id;
//    private Map<String, Integer> toppings;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", id);

        binding = ProductDetailsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView name = binding.productName;
        ImageView img = binding.productImg;
        TextView des = binding.productDes;
        TextView price = binding.productPrice;
        TextView mount = binding.productMount;
        Button add = binding.addProduct;
        Button minus = binding.minusProduct;


        binding.cancelHeader.setTitle("Sản phẩm #" + id);
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });

        ToppingAPI.all(new ToppingAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Topping> toppings) {
                Log.d("Topping", toppings.size()+"");
                for (int i =0 ; i< toppings.size();i++)
                {
                    Log.d("tencuatoppingtrongactiviti", toppings.get(i).getName());
                }

                toppinAdapter = new ToppinAdapter(DetailActivity.this, toppings);
                LinearLayoutManager manager = new LinearLayoutManager(DetailActivity.this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                binding.toppingList.setLayoutManager(manager);
                binding.toppingList.setAdapter(toppinAdapter);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soluong > 0) {
                    soluong--;
                    mount.setText(soluong + "");
                } else {
                    soluong = 0;
                    mount.setText(soluong + "");
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluong++;
                Log.d("soluong", "" + soluong);
                mount.setText(soluong + "");
            }
        });
        ProductAPI.find(key, new ProductAPI.FirebaseCallback() {
            @Override
            public void onCallback(Product product) {
                Log.d("productname", product.getName());
                name.setText(product.getName());
                des.setText(product.getDescription());
                price.setText(product.getPrice() + "");

            }
        });
        Button buyNow = binding.buyNow;
        Button addToCard = binding.addToCart;
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,PurchaseActivity.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,CartActivity.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

    }

}