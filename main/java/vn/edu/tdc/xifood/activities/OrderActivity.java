package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.databinding.OrderLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.User;
import vn.edu.tdc.xifood.views.Navbar;

public class OrderActivity extends AppCompatActivity {
    private OrderLayoutBinding binding;
    OrderAdapter orderAdapter;
    private ArrayList<Order> thisUserOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = OrderLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharePreference.setSharedPreferences(this);

        OrderAPI.all(new OrderAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Order> orders) {
                if (orders != null) {
                    thisUserOrder = new ArrayList<>();

                    String userToken = SharePreference.find(SharePreference.USER_TOKEN_KEY);

                    // Kiểm tra token không null
                    if (userToken == null || userToken.isEmpty()) {
                        Log.e("OrderActivity", "User token is null or empty");
                        return;
                    }

                    // Lấy đơn hàng của người dùng này
                    for (Order order : orders) {
                        User user = order.getUser();
                        if (user != null && user.getKey() != null && user.getKey().equals(userToken)) {
                            thisUserOrder.add(order);
                        }
                    }

                    //get this user's order
                    orderAdapter = new OrderAdapter(OrderActivity.this, thisUserOrder);
                    GridLayoutManager manager = new GridLayoutManager(OrderActivity.this, 3);
                    manager.setOrientation(RecyclerView.HORIZONTAL);

                    binding.orderList.setLayoutManager(manager);
                    binding.orderList.setAdapter(orderAdapter);

                    // thuc hien uy quyen cac nut
                    orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                        @Override
                        public void onView(View view, String key) {
                            Intent intent = new Intent(OrderActivity.this, DanhGiaActivity.class);
                            Log.d("key", "onView: " + key);
                            intent.putExtra(DanhGiaActivity.ORDERED_KEY, key);
                            startActivity(intent);
                        }

                        @Override
                        public void onBuyback(View view, String key) {
                            onBuyBackOrder(key);
                        }

                        @Override
                        public void onCancel(View view, String key) {
                            cancelOrder(key);
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

    // tao ham de xoa don hang
    private void cancelOrder(String key) {

        // tim va xoa don hang tren firebase
        final Order[] orderToCancel = new Order[1];
        for (Order order : thisUserOrder) {
            if (order.getKey().equals(key)) {
                orderToCancel[0] = order;
                break;
            }
        }

        if (orderToCancel[0] != null) {
            OrderAPI.destroy(orderToCancel[0], new OrderAPI.FirebaseCallback() {
                @Override
                public void onCallback(Order order) {
                    if (order != null) {

                        // xoa don hang khoi danh sach local
                        boolean remove = thisUserOrder.remove(orderToCancel[0]);

                        if (remove) {
                            orderAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("OrderActivity", "Không thể xóa đơn hàng khỏi danh sách local");
                        }
                    } else {
                        Log.e("OrderActivity", "Không thể xóa đơn hàng từ Firebase");
                    }
                }
            });
        } else {
            Log.e("OrderActivity", "Không tìm thấy đơn hàng để xóa");
        }
    }

    // tao ham mua lai don hang
    private void onBuyBackOrder(String key) {
        Order originalOrder = null;

        for (Order order : thisUserOrder) {
            if (order.getKey().equals(key)) {
                originalOrder = order;
                break;
            }
        }


        if (originalOrder != null) {

            Order newOrder = new Order(originalOrder);

            // them don hang mua lai vao firebase
            OrderAPI.store(newOrder, new OrderAPI.FirebaseCallback() {
                @Override
                public void onCallback(Order order) {
                    if (order != null) {

                        // Thêm đơn hàng mới vào danh sách
                        thisUserOrder.add(order);

                        // cap nhat lai giao dien
                        orderAdapter.notifyDataSetChanged();
                        Toast.makeText(OrderActivity.this, "Đơn hàng đã được mua lại", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("OrderActivity", "Không thể thêm đơn hàng mới vào Firebase");
                        Toast.makeText(OrderActivity.this, "Không thể mua lại đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Log.e("OrderActivity", "Không tìm thấy đơn hàng để mua lại");
        }
    }
}