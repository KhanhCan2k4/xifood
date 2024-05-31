package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import vn.edu.tdc.xifood.adapters.ToppinAdapter;
import vn.edu.tdc.xifood.apis.CartAPI;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.ProductAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.ToppingAPI;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.databinding.UpdateProductLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.datamodels.Product;
import vn.edu.tdc.xifood.datamodels.Topping;
import vn.edu.tdc.xifood.datamodels.User;

public class DetailUpdateProductActivity extends AppCompatActivity {

    private UpdateProductLayoutBinding binding;
    private ToppinAdapter toppingAdapter;
    private String key = "";
    private Product product;
    private Map<Topping, Integer> toppingsWithAmount;
    private Map<Topping, Integer> oldToppingsWithAmount;
    private int amount = 1;
    private ArrayList<Topping> toppings = new ArrayList<>();
    public static final String DETAIL_PRODUCT_KEY = "DETAIL_PRODUCT_KEY";
    public static final int MAX_AMOUNT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UpdateProductLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        key = intent.getStringExtra("PRODUCT_ID");
        toppingsWithAmount = new HashMap<>();
        oldToppingsWithAmount = new HashMap<>();

        // Kiểm tra xem liệu có dữ liệu cũ nào được truyền qua không
        String selectedToppings = intent.getStringExtra("SELECTED_TOPPINGS");
        // Nhận dữ liệu số lượng sản phẩm từ Intent
        final int[] oldAmount = {intent.getIntExtra("AMOUNT", 1)};

        String totalBill = intent.getStringExtra("totalBill");

        Log.d("amount", "onCreate: " + oldAmount[0]);

        // Cập nhật giao diện để hiển thị số lượng sản phẩm
        binding.productAmount.setText(String.valueOf(oldAmount[0]));
        binding.productName.setText("Đang tải...");
        binding.productPrice.setText("Đang tải...");
        binding.productDes.setText("Đang tải...");
        ImageStorageReference.setImageInto(binding.productImg, "products/default.png");
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

        ToppingAPI.all(new ToppingAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Topping> toppingsList) {
                if (toppingsList != null) {
                    toppings = toppingsList;
                    toppingAdapter = new ToppinAdapter(DetailUpdateProductActivity.this, toppings, toppingsWithAmount, new ToppinAdapter.OnToppingCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(Topping topping, boolean isChecked) {
                            if (isChecked) {
                                toppingsWithAmount.put(topping, 1);
                            } else {
                                toppingsWithAmount.remove(topping);
                            }
                        }
                    });
                    LinearLayoutManager manager = new LinearLayoutManager(DetailUpdateProductActivity.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);

                    binding.toppingList.setLayoutManager(manager);
                    binding.toppingList.setAdapter(toppingAdapter);

                    for (Topping t : toppings) {
                        toppingsWithAmount.put(t, 0);
                    }
                    // Cập nhật oldToppingsWithAmount từ Intent
                    if (selectedToppings != null && !selectedToppings.isEmpty()) {
                        String[] toppingArray = selectedToppings.split(", ");
                        for (String str : toppingArray) {
                            String[] parts = str.split(": ");
                            String toppingName = parts[0];
                            for (Topping t : toppings) {
                                if (t.getName().equals(toppingName)) {
                                    toppingsWithAmount.put(t, 1);
                                    break;
                                }
                            }
                        }

                        toppingAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        binding.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oldAmount[0] < MAX_AMOUNT) {
                    oldAmount[0]++;
                    binding.productAmount.setText(oldAmount[0] + "");
                }
            }
        });
        binding.minusProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oldAmount[0] > 1) {
                    oldAmount[0]--;
                    binding.productAmount.setText(oldAmount[0] + "");
                }
            }
        });

        binding.Capnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.Capnhat.setText("Cập nhật Thành Công");
                binding.Capnhat.setEnabled(false);

                UpdateProduct(oldAmount[0], toppingsWithAmount);
            }

        });

        if (SharePreference.findPermission() == UserAPI.STAFF_PERMISSION) {
            binding.quayLai.setVisibility(View.GONE);
        }
        binding.quayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.quayLai.setText("Đang tải...");
                binding.quayLai.setEnabled(false);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("PRODUCT_ID", key);
                returnIntent.putExtra("AMOUNT", oldAmount[0]);

                StringBuilder selectedToppingsString = new StringBuilder();
                for (Map.Entry<Topping, Integer> entry : toppingsWithAmount.entrySet()) {
                    Topping topping = entry.getKey();
                    int toppingAmount = entry.getValue();
                    if (toppingAmount > 0) {
                        selectedToppingsString.append(topping.getName()).append(": ").append(toppingAmount).append(", ");
                    }
                }
                String toppingsString = selectedToppingsString.toString();
                returnIntent.putExtra("SELECTED_TOPPINGS", toppingsString);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private void UpdateProduct(int soluong, Map<Topping, Integer> toppings) {
        Order order = new Order();
        ArrayList<OrderedProduct> products = new ArrayList<>();
        OrderedProduct orderedProduct = new OrderedProduct(product, soluong);
        Map<String, Long> orderedToppings = new HashMap<>();
        long totalToppingPrice = 0;

        for (Map.Entry<Topping, Integer> entry : toppings.entrySet()) {
            Topping topping = entry.getKey();
            int amount = entry.getValue();
            if (amount > 0 && amount <= MAX_AMOUNT) {
                orderedToppings.put(topping.getName(), (long) topping.getPrice());
                totalToppingPrice += topping.getPrice();
            }
        }
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

        long totalPrice = product.getPrice();
        Log.d("gia", "UpdateProduct: " + totalPrice);
        order.getOrderedProducts().get(0).getProduct().setPrice(totalPrice);
        order.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString());
        order.setUser(user);
        order.setStatus(Order.STATUS_WAITING);

        OnSuccessListener<Void> onSuccessListener = new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                binding.Capnhat.setText("Cập nhật sản phẩm");
                binding.Capnhat.setEnabled(true);
                showAlert("THÔNG BÁO", "Cập nhật sản phẩm thành công");

                Intent intent = new Intent();
                intent.putExtra("isCartUpdated", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        };

        OnCanceledListener onCanceledListener = new OnCanceledListener() {
            @Override
            public void onCanceled() {
                binding.Capnhat.setText("Cập nhật sản phẩm");
                binding.Capnhat.setEnabled(true);
                showAlert("THÔNG BÁO", "Đã xảy ra lỗi, vui lòng thử lại sau :<");
            }
        };

        CartAPI.update(SharePreference.find(SharePreference.USER_TOKEN_KEY), orderedProduct, onSuccessListener, onCanceledListener);
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