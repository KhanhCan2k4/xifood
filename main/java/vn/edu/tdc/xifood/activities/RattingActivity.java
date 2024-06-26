package vn.edu.tdc.xifood.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.Map;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.databinding.DanhGiaLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.datamodels.Topping;
import vn.edu.tdc.xifood.views.CancelHeader;

public class RattingActivity extends AppCompatActivity {

    private DanhGiaLayoutBinding binding;
    private OrderAdapter orderAdapter;
    private GridLayoutManager manager;
    private String key;
    private ActivityResultLauncher<String> imageReview;
    public static final String ORDERED_KEY = "ORDERED_KEY";
    private ArrayList<String> adapterVoucher;
    private Order oldOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DanhGiaLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        key = intent.getStringExtra(ORDERED_KEY);
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            Order order = (Order) bundle.getSerializable("Order");
        }

        Log.d("key 1", "onCreate: " + key);

        loadReview(key);

        OrderAPI.find(key, new OrderAPI.FirebaseCallback() {
            @Override
            public void onCallback(Order order) {
                if (order != null) {
                    long vanChuyen = Long.parseLong(binding.phiVanChuyen.getText().toString());
                    if (Order.STATUS_WAITING == order.getStatus()){
                        binding.review.setVisibility(View.GONE);
                    }
                    binding.diaChi.setText(order.getAddress());
                    if (order.getAddress() == null) {
                        binding.diaChi.setText("Bạn chưa cập nhật địa chỉ");
                    }
                    binding.ghiChu.setText(order.getNote());
                    binding.ghiChu.setEnabled(false);
                    binding.vocherList.setText(order.getVoucher());
                    if (order.getVoucher() == null){
                        binding.vocherList.setText("Bạn chưa chọn voucher");
                    }
                    binding.phuongThucThanhToan.setText(order.getPayment());
                    if (order.getPayment() == null){
                        binding.phuongThucThanhToan.setText("Bạn chưa chọn phương thức thanh toán");
                    }

                    // Calculate total topping price
                    long totalToppingPrice = 0;
                    for (OrderedProduct p : order.getOrderedProducts()) {
                        for (Map.Entry<String, Long> entry : p.getToppings().entrySet()) {
                            Topping t = new Topping();
                            t.setName(entry.getKey());
                            t.setPrice(entry.getValue());
                            totalToppingPrice += t.getPrice(); // Multiply price with amount
                            Log.d("llll", "onCallback: " + t.getPrice());
                        }
                    }


                    // Calculate total price
                    long totalPrice = 0;
                    for (OrderedProduct orderedProduct : order.getOrderedProducts()) {
                        totalPrice += orderedProduct.getAmount() * orderedProduct.getProduct().getPrice();
                    }
                    totalPrice += totalToppingPrice;
                    totalPrice += vanChuyen;
                    binding.giaDonHang.setText(totalPrice - vanChuyen+"");
                    binding.tongDonHang.setText(totalPrice+"");

                    manager = new GridLayoutManager(RattingActivity.this, 1);
                    manager.setOrientation(GridLayoutManager.HORIZONTAL);
                    orderAdapter = new OrderAdapter(RattingActivity.this, order, true);

                    binding.orderList.setLayoutManager(manager);
                    binding.orderList.setAdapter(orderAdapter);

                    binding.btnAddImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chooseImage();
                        }
                    });

                    binding.danhGia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            submitReview(order);
                        }
                    });
                } else {
                    Toast.makeText(RattingActivity.this, "Thông tin đơn hàng lỗi, vui lòng thử lại :<", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        imageReview = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    String imageReview = "reviews/" + SharePreference.find(SharePreference.USER_TOKEN_KEY);
                    ImageStorageReference.upload(imageReview, uri);
                    try {
                        ImageStorageReference.setImageInto(binding.imageReview, imageReview);
                    } catch (Exception e) {
                        //ignore
                    }
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

    private void chooseImage() {
        imageReview.launch("image/*");
    }

    private void submitReview(Order order) {
        int rating = (int) binding.rating.getRating();
        String reviewContent = binding.txtReview.getText().toString();
        String imageNameReview = "reviews/" + SharePreference.find(SharePreference.USER_TOKEN_KEY);

        if (rating == 0) {
            Toast.makeText(this, "Vui lòng đánh giá sao.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (reviewContent.isEmpty()) {
            Toast.makeText(this, "Vui lòng viết nhận xét.", Toast.LENGTH_SHORT).show();
            return;
        }

        order.setRating(rating);
        order.setReviewContent(reviewContent);
        order.setImageReview(imageNameReview);

        if (order.getKey() != null && !order.getKey().isEmpty()) {
            OrderAPI.update(order);
            disableReviewInputs();
        }
    }

    private void loadReview(String key) {
        OrderAPI.find(key, new OrderAPI.FirebaseCallback() {
            @Override
            public void onCallback(Order order) {
                if (order != null) {
                    binding.rating.setRating(order.getRating());
                    binding.txtReview.setText(order.getReviewContent());

                    String imageReview = order.getImageReview();
                    if (imageReview != null && !imageReview.isEmpty()) {
                        ImageStorageReference.setImageInto(binding.imageReview, imageReview);
                    } else {
                        binding.imageReview.setImageResource(R.drawable.img); // example placeholder
                    }

                    if (order.getRating() > 0 && !order.getReviewContent().isEmpty()) {
                        disableReviewInputs();
                    }
                } else {
                    Toast.makeText(RattingActivity.this, "Lỗi tải thông tin đánh giá.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void disableReviewInputs(){
        binding.rating.setEnabled(false);
        binding.btnAddImage.setVisibility(View.GONE);
        binding.txtReview.setEnabled(false);
        binding.danhGia.setVisibility(View.GONE);
    }
}
