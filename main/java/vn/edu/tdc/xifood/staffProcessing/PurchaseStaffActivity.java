package vn.edu.tdc.xifood.staffProcessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.databinding.PurchaseLayoutBinding;
import vn.edu.tdc.xifood.models.Order;
import vn.edu.tdc.xifood.models.Product;
import vn.edu.tdc.xifood.views.CancelHeader;

public class PurchaseStaffActivity extends AppCompatActivity {

    private PurchaseLayoutBinding binding;
    private OrderAdapter orderAdapter;
    private GridLayoutManager manager;
    private int id;
    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PurchaseLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        id = intent.getIntExtra("id", id);
//        Log.d("TAG", "onCreate: order-id" + id);

        order = new Order(1245);
        Product product1 = new Product(1, "Sản phẩm 1", "", 20000);
        Product product2 = new Product(2, "Sản phẩm 2", "", 20000);
        Product product3 = new Product(3, "Sản phẩm 3", "", 20000);
        Product product4 = new Product(4, "Sản phẩm 2", "", 20000);
        Product product5 = new Product(5, "Sản phẩm 3", "", 20000);

        product1.setAmount(2);
        product2.setAmount(5);
        product3.setAmount(6);
        product4.setAmount(2);
        product5.setAmount(1);

        order.setProducts(product1, product2, product3, product4, product5);

        manager = new GridLayoutManager(this, 1);
        manager.setOrientation(GridLayoutManager.HORIZONTAL);
//        orderAdapter = new OrderAdapter(this, order, true);

        binding.orderList.setLayoutManager(manager);
        binding.orderList.setAdapter(orderAdapter);

        binding.cancelHeader.setTitle("Đơn hàng #" + id);
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });
    }
}