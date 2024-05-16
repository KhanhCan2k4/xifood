package vn.edu.tdc.xifood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.edu.tdc.xifood.adapters.BillAdapter;
import vn.edu.tdc.xifood.adapters.ToppinAdapter;
import vn.edu.tdc.xifood.apis.CartAPI;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.apis.ProductAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.ToppingAPI;
import vn.edu.tdc.xifood.databinding.ProductDetailsLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.datamodels.Product;
import vn.edu.tdc.xifood.datamodels.User;
import vn.edu.tdc.xifood.models.Bill;
import vn.edu.tdc.xifood.datamodels.Topping;
import vn.edu.tdc.xifood.views.CancelHeader;

public class DetailActivity extends AppCompatActivity {

    private ProductDetailsLayoutBinding binding;
    private ToppinAdapter toppinAdapter;
    private String key = "";
    private Product product;
    private Map<Topping, Integer> toppingsWithAmount;
    private int amount = 1;
    public static final String DETAIL_PRODUCT_KEY = "DETAIL_PRODUCT_KEY";
    public static final int MAX_AMOUNT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductDetailsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        key = intent.getStringExtra(DETAIL_PRODUCT_KEY);
        toppingsWithAmount = new HashMap<Topping, Integer>();

        binding.productName.setText("Đang tải...");
        binding.productPrice.setText("Đang tải...");
        binding.productDes.setText("Đang tải...");
        ImageStorageReference.setImageInto(binding.productImg, "products/default.png");
        binding.productAmount.setText(amount + "");
        ProductAPI.find(key, new ProductAPI.FirebaseCallback() {
            @Override
            public void onCallback(Product p) {
                if (p != null) {
                    binding.productName.setText(p.getName());
                    binding.productPrice.setText(p.getPrice() + "");
                    binding.productDes.setText(p.getDescription());
                    ImageStorageReference.setImageInto(binding.productImg, p.getImage().get(0));
                    product = p;
                }
            }
        });

        binding = ProductDetailsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cancelHeader.setTitle("# Thông tin sản phẩm #");
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });

        //get toppingsWithAmount
        ToppingAPI.all(new ToppingAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<vn.edu.tdc.xifood.datamodels.Topping> toppings) {
                if (toppings != null) {
                    toppinAdapter = new ToppinAdapter(DetailActivity.this, toppings);
                    LinearLayoutManager manager = new LinearLayoutManager(DetailActivity.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);

                    binding.toppingList.setLayoutManager(manager);
                    binding.toppingList.setAdapter(toppinAdapter);

                    for (Topping t : toppings) {
                        toppingsWithAmount.put(t, 0);
                    }

                    toppinAdapter.setItemClick(new ToppinAdapter.ItemClick() {
                        @Override
                        public void onIncreaseAmount(ToppinAdapter.ViewHolder holder) {
                            Topping thisTopping = toppings.get(holder.getThisPosition());
                            int amount = toppingsWithAmount.get(thisTopping);
                            if (amount < MAX_AMOUNT) {
                                amount++;
                                toppingsWithAmount.put(thisTopping, amount);
                                holder.getToppingItemtBinding().totalTopping.setText(amount + "");
                            }
                        }

                        @Override
                        public void onDecreaseAmount(ToppinAdapter.ViewHolder holder) {
                            Topping thisTopping = toppings.get(holder.getThisPosition());
                            int amount = toppingsWithAmount.get(thisTopping);
                            if (amount > 0) {
                                amount--;
                                toppingsWithAmount.put(thisTopping, amount);
                                holder.getToppingItemtBinding().totalTopping.setText(amount + "");
                            }
                        }
                    });

                }
            }
        });

        //increase or decrease amount
        binding.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount < MAX_AMOUNT) {
                    amount++;
                    binding.productAmount.setText(amount + "");
                }
            }
        });
        binding.minusProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount > 1) {
                    amount--;
                    binding.productAmount.setText(amount + "");
                }
            }
        });

        //create order
        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addToCart.setText("Đang tải...");
                binding.addToCart.setEnabled(false);

                Order order = new Order();
                ArrayList<OrderedProduct> products = new ArrayList<>();
                OrderedProduct orderedProduct = new OrderedProduct(product, amount);
                Map<String, Long> orderedToppings = new HashMap<>();
                toppingsWithAmount.forEach((topping, amount) -> {
                    if (amount > 0 & amount <= MAX_AMOUNT) {
                        orderedToppings.put(topping.getName(), amount * topping.getPrice());
                    }
                });
                orderedProduct.setToppings(orderedToppings);
                products.add(orderedProduct);

                User user = new User();
                user.setKey(SharePreference.find(SharePreference.USER_TOKEN_KEY));
                user.setFullName(SharePreference.find(SharePreference.USER_NAME));
                user.setEmail(SharePreference.find(SharePreference.USER_EMAIL));
                user.setAvatar(SharePreference.find(SharePreference.USER_AVATAR));
                user.setDayOfBirth(SharePreference.find(SharePreference.USER_DOB));
                user.setGender(SharePreference.find(SharePreference.USER_GENDER));
                user.setPassword(SharePreference.find(SharePreference.USER_PASS));
                user.setPermistion(SharePreference.findPermission());

                order.setOrderedProducts(products);
                order.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString());
                order.setUser(user);
                order.setStatus(Order.STATUS_WAITING);

                OnSuccessListener onSuccessListener = new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        binding.addToCart.setText("Thêm vào giỏ hàng");
                        binding.addToCart.setEnabled(true);
                        showAlert("THÔNG BÁO", "Thêm vào giỏ hàng thành công");
                    }
                };
                OnCanceledListener onCanceledListener = new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        binding.addToCart.setText("Thêm vào giỏ hàng");
                        binding.addToCart.setEnabled(true);
                        showAlert("THÔNG BÁO", "Đã xảy ra lỗi, vui lòng thử lại sau :<");
                    }
                };
                //store into server
                CartAPI.store(order, onSuccessListener, onCanceledListener);
            }
        });

        binding.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buyNow.setText("Đang tải...");
                binding.buyNow.setEnabled(false);

                Order order = new Order();
                ArrayList<OrderedProduct> products = new ArrayList<>();
                OrderedProduct orderedProduct = new OrderedProduct(product, amount);
                Map<String, Long> orderedToppings = new HashMap<>();
                toppingsWithAmount.forEach((topping, amount) -> {
                    if (amount > 0 & amount <= MAX_AMOUNT) {
                        orderedToppings.put(topping.getName(), amount * topping.getPrice());
                    }
                });
                orderedProduct.setToppings(orderedToppings);
                products.add(orderedProduct);

                User user = new User();
                user.setKey(SharePreference.find(SharePreference.USER_TOKEN_KEY));
                user.setFullName(SharePreference.find(SharePreference.USER_NAME));
                user.setEmail(SharePreference.find(SharePreference.USER_EMAIL));
                user.setAvatar(SharePreference.find(SharePreference.USER_AVATAR));
                user.setDayOfBirth(SharePreference.find(SharePreference.USER_DOB));
                user.setGender(SharePreference.find(SharePreference.USER_GENDER));
                user.setPassword(SharePreference.find(SharePreference.USER_PASS));
                user.setPermistion(SharePreference.findPermission());

                order.setOrderedProducts(products);
                order.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString());
                order.setUser(user);
                order.setStatus(Order.STATUS_WAITING);

                //store into server
                OrderAPI.store(order)
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                binding.buyNow.setText("Mua ngay");
                                binding.buyNow.setEnabled(true);
                                Intent intent = new Intent(DetailActivity.this, PurchaseActivity.class);
                                intent.putExtra(PurchaseActivity.ORDERED_KEY, order.getKey());
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                                startActivity(intent);
                            }
                        })
                        .addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                binding.buyNow.setText("Mua ngay");
                                binding.buyNow.setEnabled(true);
                                showAlert("THÔNG BÁO", "Đã xảy ra lỗi, vui lòng thử lại sau :<");
                            }
                        });
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}