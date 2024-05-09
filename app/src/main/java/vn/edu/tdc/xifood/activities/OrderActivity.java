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
import vn.edu.tdc.xifood.models.Order;
import vn.edu.tdc.xifood.models.Product;
import vn.edu.tdc.xifood.views.Navbar;

public class OrderActivity extends AppCompatActivity {
    private OrderLayoutBinding binding;
    private ArrayList<Order> orders;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = OrderLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        orders = new ArrayList<>();
        Order order1 = new Order(123);
        Order order2 = new Order(124);
        Order order3 = new Order(125);

        Product product1 = new Product(1, "Sản phẩm 1", "=", 20000);
        Product product2 = new Product(2, "Sản phẩm 2", "=", 20000);
        Product product3 = new Product(3, "Sản phẩm 3", "=", 20000);
        Product product4 = new Product(4, "Sản phẩm 2", "=", 20000);
        Product product5 = new Product(5, "Sản phẩm 3", "=", 20000);

        product1.setAmount(2);
        product2.setAmount(5);
        product3.setAmount(6);
        product4.setAmount(2);
        product5.setAmount(1);

        order1.setProducts(product1, product3);
        order2.setProducts(product1, product2, product3, product4, product5);
        order3.setProducts(product1, product3, product4, product5);

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        orderAdapter = new OrderAdapter(this, orders);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(RecyclerView.HORIZONTAL);

        binding.orderList.setLayoutManager(manager);
        binding.orderList.setAdapter(orderAdapter);

        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onView(View view, int id) {
                Intent intent = new Intent(OrderActivity.this, PurchaseActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }

            @Override
            public void onBuyback(View view, int id) {
                Intent intent = new Intent(OrderActivity.this, PurchaseActivity.class);
                intent.putExtra("id", id);

                startActivity(intent);
            }

            @Override
            public void onCancel(View view, int id) {

            }
        });
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