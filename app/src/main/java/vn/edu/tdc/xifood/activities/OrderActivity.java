package vn.edu.tdc.xifood.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.databinding.OrderLayoutBinding;
import vn.edu.tdc.xifood.models.Order;
import vn.edu.tdc.xifood.models.Product;

public class OrderActivity extends AppCompatActivity {
    private OrderLayoutBinding binding;
    private ArrayList<Order> orders;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = OrderLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.cancelHeader.setTitle("Đơn mua");

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
    }
}