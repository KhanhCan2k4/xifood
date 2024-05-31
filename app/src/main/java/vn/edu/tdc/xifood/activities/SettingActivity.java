package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.ListProductsAdapter;
import vn.edu.tdc.xifood.adapters.RecentsProductsAdapter;
import vn.edu.tdc.xifood.datamodels.Product;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.databinding.SettingLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.User;
import vn.edu.tdc.xifood.views.Navbar;

public class SettingActivity extends AppCompatActivity {
    private SettingLayoutBinding binding;
    private RecentsProductsAdapter adapter;
    private ArrayList<Product> boughts;
    private ListProductsAdapter listProductsAdapter;

    private ArrayList<Product> products = new ArrayList<>();


    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //lay nguoi dung tu SharePreference
        SharePreference.setSharedPreferences(SettingActivity.this); // phai co moi chay nhen ong co

        //Logout
        // Trong phương thức onCreate của bạn
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("Đăng Xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                        .setPositiveButton("Đăng Xuất", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Xóa tất cả dữ liệu người dùng
                                SharePreference.clearAll();
                                // Chuyển hướng người dùng
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
binding.btnContact.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(SettingActivity.this, "Vui lòng liên hệ 19008080 để được hỗ trợ", Toast.LENGTH_LONG).show();

    }
});
binding.btnRate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SettingActivity.this, RatingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // chuyen
        startActivity(intent);
    }
});
binding.btnVoucher.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(SettingActivity.this, "tính năng đang được cập nhật", Toast.LENGTH_LONG).show();

    }
});
binding.btnOrderHistory.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SettingActivity.this, OrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // chuyen
        startActivity(intent);
    }
});
binding.btnCart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SettingActivity.this, CartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // chuyen
        startActivity(intent);
    }
});

        binding.username.setText(SharePreference.find(SharePreference.USER_NAME));
        try {
            ImageStorageReference.setImageInto(binding.imageAvatar,
                    "avatars/default.jpg");
            ImageStorageReference.setImageInto(binding.imageAvatar,
                    SharePreference.find(SharePreference.USER_AVATAR));
        } catch (Exception e) {
            //ignore
        }

        products = new ArrayList<>();

//        adapter = new RecentsProductsAdapter(this, products);

        Log.d("product", products.size() + "");

        //Tạo đối tượng layout Mânger
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recentsSettingRecycleView.setLayoutManager(layoutManager);

        binding.recentsSettingRecycleView.setAdapter(adapter);

        binding.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, AccountActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                // chuyen
                startActivity(intent);
            }
        });

        binding.navbar.setNavClickListener(new Navbar.OnNavClickListener() {
            @Override
            public void onHomeButtonClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onDiscountButtonClick(View view) {
                //chuyen qua danh muc uu dai
                Intent intent = new Intent(SettingActivity.this, ListProductsActivity.class);
                intent.putExtra("id", 1);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onOrderButtonClick(View view) {
                Intent intent = new Intent(SettingActivity.this, OrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // chuyen
                startActivity(intent);
            }

            @Override
            public void onAccountButtonClick(View view) {
                //ignore
            }
        });

        OrderAPI.all(new OrderAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Order> orders) {
                if (orders != null) {
                    boughts = new ArrayList<>();

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
                            Product p = order.getOrderedProducts().get(0).getProduct();
                            boughts.add(p);
                        }
                    }

                    //get this user's order
                    listProductsAdapter = new ListProductsAdapter(SettingActivity.this, boughts);
                    GridLayoutManager manager = new GridLayoutManager(SettingActivity.this, 2);
                    manager.setOrientation(RecyclerView.VERTICAL);

                    binding.recentsSettingRecycleView.setLayoutManager(manager);
                    binding.recentsSettingRecycleView.setAdapter(listProductsAdapter);
                    listProductsAdapter.setItemClickListener(new ListProductsAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(ListProductsAdapter.ViewHolder holder) {
                            Intent intent = new Intent(SettingActivity.this, DetailActivity.class);
                            intent.putExtra(DetailActivity.DETAIL_PRODUCT_KEY, holder.getProductKey());

                            startActivity(intent);
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        binding.username.setText(SharePreference.find(SharePreference.USER_NAME));
        try {
            ImageStorageReference.setImageInto(binding.imageAvatar,
                    "avatars/default.jpg");
            ImageStorageReference.setImageInto(binding.imageAvatar,
                    SharePreference.find(SharePreference.USER_AVATAR));
        } catch (Exception e) {
            //ignore
        }
    }
}