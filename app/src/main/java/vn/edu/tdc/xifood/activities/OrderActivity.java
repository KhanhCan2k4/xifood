package vn.edu.tdc.xifood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.databinding.OrderLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.views.Navbar;

public class OrderActivity extends AppCompatActivity {
    private OrderLayoutBinding binding;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> thisUserOrder;
    private Order orderM;
    private String thanhToan;
    private String vocher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharePreference.setSharedPreferences(this);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();


        // Hiển thị danh sách đơn hàng của người dùng
        showUserOrders();

        // Xử lý sự kiện khi nhấn nút trở về
        binding.navbar.setNavClickListener(new Navbar.OnNavClickListener() {
            @Override
            public void onHomeButtonClick(View view) {
                navigateTo(MainActivity.class);
            }

            @Override
            public void onDiscountButtonClick(View view) {
                navigateTo(ListProductsActivity.class);
            }

            @Override
            public void onOrderButtonClick(View view) {
                // Do nothing
            }

            @Override
            public void onAccountButtonClick(View view) {
                navigateTo(SettingActivity.class);
            }
        });
    }

    // Phương thức hiển thị danh sách đơn hàng của người dùng
    private void showUserOrders() {
        OrderAPI.all(new OrderAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Order> orders) {
                if (orders != null) {
                    thisUserOrder = new ArrayList<>();
                    String userToken = SharePreference.find(SharePreference.USER_TOKEN_KEY);
                    if (userToken == null || userToken.isEmpty()) {
                        Log.e("OrderActivity", "User token is null or empty");
                        return;
                    }
                    for (Order order : orders) {
                        if (order.getUser() != null && order.getUser().getKey() != null && order.getUser().getKey().equals(userToken)) {
                            thisUserOrder.add(order);
                        }
                    }
                    setupRecyclerView();
                }
            }
        });
    }

    // Phương thức cấu hình RecyclerView hiển thị danh sách đơn hàng
    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(OrderActivity.this, thisUserOrder);
        GridLayoutManager manager = new GridLayoutManager(OrderActivity.this, 3);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        binding.orderList.setLayoutManager(manager);
        binding.orderList.setAdapter(orderAdapter);

        // Xử lý sự kiện click trên các item trong RecyclerView
        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onView(View view, String key) {
                OrderAPI.find(key, new OrderAPI.FirebaseCallback() {
                    @Override
                    public void onCallback(Order order) {
                        if (order.getStatus() == 2){
                            navigateTo(RatingActivity.class, RatingActivity.ORDERED_KEY, key);
                        }
                        else {
                            navigateTo(PayingActivity.class, RatingActivity.ORDERED_KEY, key);
                        }
                    }
                });

            }

            @Override
            public void onBuyback(View view, String key) {
                buyBackOrder(key);
            }

            @Override
            public void onCancel(View view, String key) {
                cancelOrder(key);
            }
        });
    }

    // Phương thức chuyển đến một Activity khác
    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(OrderActivity.this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    // Phương thức chuyển đến một Activity khác và truyền dữ liệu
    private void navigateTo(Class<?> cls, String key, String value) {
        Intent intent = new Intent(OrderActivity.this, cls);
        intent.putExtra(key, value);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
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
    private void buyBackOrder(String key) {
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
//                        thisUserOrder.add(order);

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