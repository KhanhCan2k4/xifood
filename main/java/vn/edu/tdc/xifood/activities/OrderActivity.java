package vn.edu.tdc.xifood.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.OrderLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.Order;
import vn.edu.tdc.xifood.views.Navbar;

public class OrderActivity extends AppCompatActivity {
    private OrderLayoutBinding binding;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> thisUserOrder;
    private API<Order> orderAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharePreference.setSharedPreferences(this);

        //setup recyclerview
        thisUserOrder = new ArrayList<>();
        orderAdapter = new OrderAdapter(OrderActivity.this, thisUserOrder);
        LinearLayoutManager manager = new LinearLayoutManager(OrderActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.orderList.setLayoutManager(manager);
        binding.orderList.setAdapter(orderAdapter);

        // Xử lý sự kiện click trên các item trong RecyclerView
        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onView(View view, String key) {
               Intent intent = new Intent(OrderActivity.this, PurchaseActivity.class);
               intent.putExtra(PurchaseActivity.IS_VIEW_MODE_KEY, true);
               intent.putExtra(PurchaseActivity.ORDERED_KEY, key);

               startActivity(intent);
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

        // Hiển thị danh sách đơn hàng của người dùng
        orderAPI = new API<>(Order.class, API.ORDER_TABLE_NAME);

        orderAPI.all(new API.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList list) {
                thisUserOrder.clear();

                for (Object o: list ) {
                    Order order = (Order) o;

                    if (!order.getStatus().equals("OD_01")) {
                        thisUserOrder.add(0, order);
                    }
                }

                orderAdapter.notifyDataSetChanged();
            }
        });

        // Xử lý sự kiện khi nhấn nút trở về
        binding.navbar.setActiveIndex(1);
        binding.navbar.setNavClickListener(new Navbar.OnNavClickListener() {
            @Override
            public void onHomeButtonClick(View view) {
                navigateTo(MainActivity.class);
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

    // Phương thức chuyển đến một Activity khác
    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(OrderActivity.this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    // tao ham de xoa don hang
    private void cancelOrder(String key) {
        //confirm to cancel order
        new AlertDialog.Builder(OrderActivity.this)
                .setIcon(R.drawable.order_icon)
                .setTitle("Huỷ đơn hàng")
                .setMessage("Bạn muốn xoá đơn hàng này?")
                .setNegativeButton("Bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                })
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // find and change order's status in firebase
                        orderAPI.find(key, new API.FirebaseCallback() {
                            @Override
                            public void onCallback(Object object) {
                                if (object != null) {
                                    Order order = (Order) object;

                                    order.setStatus("OD_04");

                                    orderAPI.update(order, order.getKey(),
                                            new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    new AlertDialog.Builder(OrderActivity.this)
                                                            .setIcon(R.drawable.order_icon)
                                                            .setTitle("Huỷ đơn hàng")
                                                            .setMessage("Bạn vừa huỷ 1 đơn hàng")
                                                            .show();
                                                }
                                            },
                                            new OnCanceledListener() {
                                                @Override
                                                public void onCanceled() {
                                                    new AlertDialog.Builder(OrderActivity.this)
                                                            .setIcon(R.drawable.order_icon)
                                                            .setTitle("Huỷ đơn hàng")
                                                            .setMessage("Huỷ đơn hàng không thành công")
                                                            .show();
                                                }
                                            },
                                            new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    //ignore
                                                }
                                            }
                                    );
                                }
                            }
                        });
                    }
                })
                .show();
    }

    // tao ham mua lai don hang
    private void buyBackOrder(String key) {
        // find original order in firebase
        orderAPI.find(key, new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    Order order = (Order) object;

                    //copy a new order
                    Order newOrder = new Order();
                    newOrder.setOrderedProducts((ArrayList<Order.OrderedProduct>) order.getOrderedProducts().clone());

                    //save into firebase
                    String key = orderAPI.store(newOrder,
                            new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    new AlertDialog.Builder(OrderActivity.this)
                                            .setIcon(R.drawable.order_icon)
                                            .setTitle("Mua lại đơn hàng")
                                            .setMessage("Bạn vừa mua lại 1 đơn hàng")
                                            .show();
                                }
                            },
                            new OnCanceledListener() {
                                @Override
                                public void onCanceled() {
                                    new AlertDialog.Builder(OrderActivity.this)
                                            .setIcon(R.drawable.order_icon)
                                            .setTitle("Mua lại đơn hàng")
                                            .setMessage("Không thể mua lại đơn này")
                                            .show();
                                }
                            },
                            new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                }
                            }
                    );

                    Intent intent = new Intent(OrderActivity.this, PurchaseActivity.class);
                    intent.putExtra(PurchaseActivity.ORDERED_KEY, key);

                    startActivity(intent);
                }
            }
        });
    }
}