package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.adapters.ListProductsAdapter;
import vn.edu.tdc.xifood.mydatamodels.Product;
import vn.edu.tdc.xifood.ImageStorageReference;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.SettingLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.Order;
import vn.edu.tdc.xifood.mydatamodels.User;
import vn.edu.tdc.xifood.views.Navbar;

public class SettingActivity extends AppCompatActivity {
    private SettingLayoutBinding binding;
    private ListProductsAdapter listProductsAdapter;
    private ArrayList<Product> products = new ArrayList<>();
    private API<String> configAPI;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //lay nguoi dung tu SharePreference
        SharePreference.setSharedPreferences(SettingActivity.this);
        configAPI = new API<>(String.class, API.CONFIG_TABLE_NAME);

        View.OnClickListener onClickListener = new View.OnClickListener() {
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
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };

        //Logout
        // Trong phương thức onCreate của bạn
        binding.btnLogout.setOnClickListener(onClickListener);
        binding.logout.setOnClickListener(onClickListener);
        binding.btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "Vui lòng liên hệ 19008080 để được hỗ trợ", Toast.LENGTH_LONG).show();

            }
        });

        configAPI.find("APP_LINK", new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    String appLink = (String) object;
                    binding.btnWeb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(appLink));

                            startActivity(intent);
                        }
                    });
                }
            }
        });

        configAPI.find("CONTACT_LINK", new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    String appLink = (String) object;
                    binding.btnContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(appLink));

                            startActivity(intent);
                        }
                    });
                }
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
        ImageStorageReference.setImageInto(binding.imageAvatar,
                "avatars/" + SharePreference.find(SharePreference.USER_TOKEN_KEY));

        //Tạo đối tượng layout Mânger
        listProductsAdapter = new ListProductsAdapter(SettingActivity.this, new ArrayList<>());
        LinearLayoutManager manager = new LinearLayoutManager(SettingActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
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

        binding.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, AccountActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                // chuyen
                startActivity(intent);
            }
        });
        binding.btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, AccountActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                // chuyen
                startActivity(intent);
            }
        });

        binding.navbar.setActiveIndex(2);
        binding.navbar.setNavClickListener(new Navbar.OnNavClickListener() {
            @Override
            public void onHomeButtonClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
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

        API<Order> orderAPI = new API<>(Order.class, API.ORDER_TABLE_NAME);
        API<Product> productAPI = new API<>(Product.class, API.PRODUCT_TABLE_NAME);

        orderAPI.all(new API.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList orders) {
                products = new ArrayList<>();
                for (Object o : orders) {
                    Order order = (Order) o;

                    for (Order.OrderedProduct orderedProduct : order.getOrderedProducts()) {
                        productAPI.find(orderedProduct.getKey(), new API.FirebaseCallback() {
                            @Override
                            public void onCallback(Object object) {
                                Product product = (Product) object;

                                if (!products.contains(product)) {
                                    products.add(0, product);
                                }
                            }
                        });
                    }
                }

                listProductsAdapter.setProducts(products);
                listProductsAdapter.notifyDataSetChanged();
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
                    SharePreference.find(SharePreference.USER_AVATAR));
        } catch (Exception e) {
            //ignore
        }
    }
}