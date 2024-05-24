package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.databinding.PurchaseLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.models.Product;
import vn.edu.tdc.xifood.views.CancelHeader;

public class PurchaseActivity extends AppCompatActivity {
    private PurchaseLayoutBinding binding;
    private OrderAdapter orderAdapter;
    private GridLayoutManager manager;
    private String key;
    private Order order;
    public static final String ORDERED_KEY = "ORDERED_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PurchaseLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        key = intent.getStringExtra(ORDERED_KEY);

        OrderAPI.find(key, new OrderAPI.FirebaseCallback() {
            @Override
            public void onCallback(Order order) {
                if (order != null) {
                    manager = new GridLayoutManager(PurchaseActivity.this, 1);
                    manager.setOrientation(GridLayoutManager.HORIZONTAL);
                    orderAdapter = new OrderAdapter(PurchaseActivity.this, order, true);

                    binding.orderList.setLayoutManager(manager);
                    binding.orderList.setAdapter(orderAdapter);
                } else {
                    Toast.makeText(PurchaseActivity.this, "Thông tin đơn hàng lỗi, vui lòng thử lại :<", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        binding.cancelHeader.setTitle("# Thông tin đơn hàng #");
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });
    }

}