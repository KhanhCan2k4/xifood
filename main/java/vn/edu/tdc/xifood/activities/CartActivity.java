package vn.edu.tdc.xifood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import vn.edu.tdc.xifood.adapters.CartAdapter;
import vn.edu.tdc.xifood.apis.CartAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.databinding.ActionFooterLayoutBinding;
import vn.edu.tdc.xifood.databinding.ActivityCartLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.views.CancelHeader;

public class CartActivity extends AppCompatActivity {

    private ActivityCartLayoutBinding binding;
    private ActionFooterLayoutBinding bindingFooter;
    private ArrayList<OrderedProduct> orders = new ArrayList<>();
    private CartAdapter adapter;
    private String userId;
    private static final int REQUEST_CODE_UPDATE_PRODUCT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartLayoutBinding.inflate(getLayoutInflater());
        bindingFooter = ActionFooterLayoutBinding.bind(binding.cartFooter);
        setContentView(binding.getRoot());

        // Initialize SharedPreferences
        SharePreference.setSharedPreferences(this);

        userId = SharePreference.find(SharePreference.USER_TOKEN_KEY);

        // Initialize adapter and LinearLayoutManager
        adapter = new CartAdapter(this, orders, userId);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listCart.setLayoutManager(manager);
        binding.listCart.setAdapter(adapter);
        binding.cancelHeader.setTitle("# Giỏ hàng #");
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        // Display the total bill


    }

    private void updateTotalBill(long total) {
        bindingFooter.txtTotal.setText(String.format("%d VND", total));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE_PRODUCT && resultCode == RESULT_OK && data != null) {
            String productId = data.getStringExtra("PRODUCT_ID");
            int amount = data.getIntExtra("AMOUNT", 1);
            String selectedToppings = data.getStringExtra("SELECTED_TOPPINGS");

            for (OrderedProduct orderedProduct : orders) {
                if (orderedProduct.getProduct().getKey().equals(productId)) {
                    orderedProduct.setAmount(amount);

                    Map<String, Long> orderedToppings = new HashMap<>();
                    if (selectedToppings != null && !selectedToppings.isEmpty()) {
                        String[] toppingArray = selectedToppings.split(", ");
                        for (String str : toppingArray) {
                            String[] parts = str.split(": ");
                            String toppingName = parts[0];
                            long toppingPrice = Long.parseLong(parts[1]);
                            orderedToppings.put(toppingName, toppingPrice);
                        }
                    }
                    orderedProduct.setToppings(orderedToppings);
                    updateFirebase(orderedProduct);
                    break;
                }
            }
            // Cập nhật lại totalBill
            long totalBill = 0;
            for (OrderedProduct orderedProduct : orders) {
                if (orderedProduct.isCheckedPay() == true) {
                    totalBill += orderedProduct.getProduct().getPrice() * orderedProduct.getAmount();
                    for (Map.Entry<String, Long> entry : orderedProduct.getToppings().entrySet()) {
                        totalBill += entry.getValue();
                    }
                }
            }
            updateTotalBill(totalBill);
            adapter.notifyDataSetChanged();
        }
    }

    private void updateFirebase(OrderedProduct orderedProduct) {
        CartAPI.update(userId, orderedProduct, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("CartActivity", "Order updated successfully.");
            }
        }, new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e("CartActivity", "Failed to update order.");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve userId from SharedPreferences
        CartAPI.find(userId, new CartAPI.FirebaseCallback() {
            @Override
            public void onCallback(Order order) {
                if (order != null && order.getOrderedProducts() != null) {
                    // Update cart items
                    orders.clear();
                    orders.addAll(order.getOrderedProducts());
                    adapter.notifyDataSetChanged();
                    adapter.setTotalBillUpdateListener(new CartAdapter.OnTotalBillUpdateListener() {
                        @Override
                        public void onTotalBillUpdate(long totalBill) {
                            updateTotalBill(totalBill);
                        }
                    });
                    Log.d("CartActivity", "onCallback: " + orders);
                } else {
                    Log.d("CartActivity", "Null");
                }
            }
        });
    }
}
