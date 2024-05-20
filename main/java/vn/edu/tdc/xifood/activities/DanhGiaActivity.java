package vn.edu.tdc.xifood.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import okhttp3.ResponseBody;
import vn.edu.tdc.xfood.R;
import vn.edu.tdc.xfood.databinding.DanhGiaLayoutBinding;
import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.views.ActionFooter;
import vn.edu.tdc.xifood.views.CancelHeader;

public class DanhGiaActivity extends AppCompatActivity {

    private DanhGiaLayoutBinding binding;
    private OrderAdapter orderAdapter;
    private GridLayoutManager manager;
    private String key;
    private ActivityResultLauncher<String> imageReview;
    public static final String ORDERED_KEY = "ORDERED_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.danh_gia_layout);
        binding = DanhGiaLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        key = intent.getStringExtra(ORDERED_KEY);

        Log.d("key 1", "onCreate: " + key);

        loadReview(key);

        OrderAPI.find(key, new OrderAPI.FirebaseCallback() {
            @Override
            public void onCallback(Order order) {

                if (order != null) {
                    Log.d("test", "onCallback: ");
                    if (Order.STATUS_WAITING == order.getStatus()){
                        binding.review.setVisibility(View.GONE);
                    }
                    manager = new GridLayoutManager(DanhGiaActivity.this, 1);
                    manager.setOrientation(GridLayoutManager.HORIZONTAL);
                    orderAdapter = new OrderAdapter(DanhGiaActivity.this, order, true);

                    binding.orderList.setLayoutManager(manager);
                    binding.orderList.setAdapter(orderAdapter);

                    // nhi
                    binding.btnAddImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chooseImage();
                        }
                    });

                    // ấn dô nút, thì sẽ đánh giá
                    binding.danhGia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            submitReview(order);
                        }
                    });


                } else {
                    Toast.makeText(DanhGiaActivity.this, "Thông tin đơn hàng lỗi, vui lòng thử lại :<", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        imageReview = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
//                Log.d("image", "onActivityResult: kkkkkkkkkkkk");
                if (uri != null) {

                    // Display the image or get the image path
                    String imageReview = "reviews/" + SharePreference.find(SharePreference.USER_TOKEN_KEY);
                    Log.d("image", "onActivityResult: " + imageReview);

                    //Update into server
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

//        Log.d("danhGia", "submitReview: ");

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

        // set cac du lieu danh gia
        order.setRating(rating);
        order.setReviewContent(reviewContent);
        order.setImageReview(imageNameReview);

        // Kiểm tra xem order có key chưa
        if (order.getKey() != null && !order.getKey().isEmpty()) {
            // update đơn hàng hiện tại
            OrderAPI.update(order);
            // Vô hiệu hóa các thành phần giao diện sau khi đánh giá được gửi
            disableReviewInputs();
        }

        // update
//        OrderAPI.update(order);

        // khi an danh gia thi se khong cho chon de danh gia nua
//        binding.rating.setEnabled(false);
//        binding.btnAddImage.setVisibility(View.GONE);
//        binding.txtReview.setEnabled(false);
//        binding.danhGia.setVisibility(View.GONE);
    }

    private void loadReview(String key) {
        Log.d("loadReview", "Loading review with key: " + key);

        OrderAPI.find(key, new OrderAPI.FirebaseCallback() {
            @Override
            public void onCallback(Order order) {
                if (order != null) {
                    Log.d("loadReview", "Order found: " + order.getKey());
                    binding.rating.setRating(order.getRating());
                    binding.txtReview.setText(order.getReviewContent());

                    String imageReview = order.getImageReview();
                    if (imageReview != null && !imageReview.isEmpty()) {
                        ImageStorageReference.setImageInto(binding.imageReview, imageReview);
                    } else {
                        Log.w("loadReview", "Image review path is null or empty");
                        // Optionally set a default or placeholder image here
                        binding.imageReview.setImageResource(R.drawable.img); // example placeholder
                    }

                    if (order.getRating() > 0 && !order.getReviewContent().isEmpty()) {
                        disableReviewInputs();
                    }
                } else {
                    Log.e("loadReview", "Order not found for key: " + key);
                    Toast.makeText(DanhGiaActivity.this, "Lỗi tải thông tin đánh giá.", Toast.LENGTH_SHORT).show();
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