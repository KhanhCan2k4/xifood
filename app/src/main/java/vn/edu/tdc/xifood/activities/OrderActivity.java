package vn.edu.tdc.xifood.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.databinding.OrderLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.models.Product;
import vn.edu.tdc.xifood.views.Navbar;

public class OrderActivity extends AppCompatActivity {
    private OrderLayoutBinding binding;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = OrderLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        OrderAPI.all(new OrderAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Order> orders) {
                if (orders != null) {
                    orderAdapter = new OrderAdapter(OrderActivity.this, orders);
                    GridLayoutManager manager = new GridLayoutManager(OrderActivity.this, 3);
                    manager.setOrientation(RecyclerView.HORIZONTAL);

                    binding.orderList.setLayoutManager(manager);
                    binding.orderList.setAdapter(orderAdapter);

                    orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                        @Override
                        public void onView(View view, String key) {
                            Intent intent = new Intent(OrderActivity.this, PurchaseActivity.class);
                            intent.putExtra(PurchaseActivity.ORDERED_KEY, key);

                            startActivity(intent);
                        }

                        @Override
                        public void onBuyback(View view, String key) {
                            Intent intent = new Intent(OrderActivity.this, PurchaseActivity.class);
                            intent.putExtra("id", key);

                            startActivity(intent);
                        }

                        @Override
                        public void onCancel(View view, String key) {

                        }
                    });

                }
            }
        });

        binding.navbar.setNavClickListener(new Navbar.OnNavClickListener() {
            @Override
            public void onHomeButtonClick(View view) {
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onDiscountButtonClick(View view) {
                //chuyen qua danh muc uu dai
                Intent intent = new Intent(OrderActivity.this, ListProductsActivity.class);
                intent.putExtra("id", 1);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onOrderButtonClick(View view) {
                //ignore
            }

            @Override
            public void onAccountButtonClick(View view) {
                Intent intent = new Intent(OrderActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }
        });

    }
}