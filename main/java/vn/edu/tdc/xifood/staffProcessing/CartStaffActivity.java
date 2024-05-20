package vn.edu.tdc.xifood.staffProcessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import vn.edu.tdc.xfood.databinding.ActivityCartLayoutBinding;
import vn.edu.tdc.xifood.activities.CartActivity;
import vn.edu.tdc.xifood.activities.ListProductsActivity;
import vn.edu.tdc.xifood.activities.MainActivity;
import vn.edu.tdc.xifood.activities.OrderActivity;
import vn.edu.tdc.xifood.adapters.CartAdapter;
import vn.edu.tdc.xifood.data.CartData;
import vn.edu.tdc.xifood.models.Cart;
import vn.edu.tdc.xifood.views.Navbar;

public class CartStaffActivity extends AppCompatActivity {
    private ActivityCartLayoutBinding binding;
    private ArrayList<Cart> carts;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        carts = CartData.getCarts();

        adapter = new CartAdapter(this, carts);
        Log.d("Cart", carts.size() + "");
        LinearLayoutManager manager = new LinearLayoutManager(CartStaffActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.listCart.setLayoutManager(manager);
        binding.listCart.setAdapter(adapter);
//        toppinAdapter = new ToppinAdapter(this, toppings);
//        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.navbar.setNavClickListener(new Navbar.OnNavClickListener() {
            @Override
            public void onHomeButtonClick(View view) {
                Intent intent = new Intent(CartStaffActivity.this, MainStaffActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

            @Override
            public void onDiscountButtonClick(View view) {
                Intent intent = new Intent(CartStaffActivity.this, MainStaffActivity.class);
                intent.putExtra("id", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

            @Override
            public void onOrderButtonClick(View view) {
                Intent intent = new Intent(CartStaffActivity.this, OrderStaffActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

            @Override
            public void onAccountButtonClick(View view) {
                //ignore
            }
        });

    }
}