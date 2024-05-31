package vn.edu.tdc.xifood.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Map;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.apis.PaymentAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.databinding.AccountLayoutBinding;
import vn.edu.tdc.xifood.databinding.DanhGiaLayoutBinding;
import vn.edu.tdc.xifood.databinding.PaymentLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.datamodels.Payment;
import vn.edu.tdc.xifood.datamodels.Topping;
import vn.edu.tdc.xifood.views.CancelHeader;

public class PayingActivity extends AppCompatActivity {
    private PaymentLayoutBinding binding;
    private OrderAdapter orderAdapter;
    private GridLayoutManager manager;
    private String key;
    private ActivityResultLauncher<String> imageReview;
    public static final String ORDERED_KEY = "ORDERED_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = PaymentLayoutBinding.inflate(
                getLayoutInflater()
        );
        setContentView(binding.getRoot());
        final String[] nameT = {""};
        final String[] priceT = {""};
        final long[] price = {};

        Intent intent = getIntent();
        key = intent.getStringExtra(ORDERED_KEY);

        binding.cancelHeader.setTitle("# Thanh Toán #");
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });
        OrderAPI.find(key, new OrderAPI.FirebaseCallback() {
            @Override
            public void onCallback(Order order) {
                if (order != null) {
                    long vanChuyen = Long.parseLong(binding.phiVanChuyen.getText().toString());
                    binding.diaChi.setText(order.getAddress() != null ? order.getAddress() : "Bạn chưa cập nhật địa chỉ");
                    binding.ghiChu.setText(order.getNote());
                    binding.ghiChu.setEnabled(false);
                    binding.vocherList.setText(order.getVoucher() != null ? order.getVoucher() : "Bạn chưa chọn voucher");
                    binding.phuongThucThanhToan.setText(order.getPayment() != null ? order.getPayment() : "Bạn chưa chọn phương thức thanh toán");

                    if (order.getPayment() != null) {
                        Intent intent = getIntent();
//                        long tongGia = Long.parseLong(intent.getStringExtra("resultPayment"));
                        long tongGia = 0;
                        if (order.getPayment().equals("Thanh toán Momo")) {
                            Log.d("RES", "onCallback: MOMO");
                            PaymentAPI.find(PaymentAPI.MOMO_KEY, new PaymentAPI.FirebaseCallback() {
                                @Override
                                public void onCallback(Payment payment) {
                                    ImageStorageReference.setImageInto(binding.phuongThucThanhToanImage, payment.getQr());
                                }
                            });

                        }
                        else if (order.getPayment().equals("Thanh toán bằng Ngân Hàng")) {
                            Log.d("RES", "onCallback: NGAN HANG");
                            PaymentAPI.find(PaymentAPI.SACOMBANK_KEY, new PaymentAPI.FirebaseCallback() {
                                @Override
                                public void onCallback(Payment payment) {
                                    ImageStorageReference.setImageInto(binding.phuongThucThanhToanImage, payment.getQr());
                                }
                            });
                        }
                    }
                    for (OrderedProduct p : order.getOrderedProducts()) {
                        for (Map.Entry<String, Long> entry : p.getToppings().entrySet()) {
                            Topping t = new Topping();
                            t.setName(entry.getKey());
                            t.setPrice(entry.getValue());
                            nameT[0] = nameT[0] + t.getName() + "\n";
                            priceT[0] = priceT[0] + t.getPrice() + "\n";
                        }
                    }
                    binding.giaTopping.setText(priceT[0]);
                    binding.nameTopping.setText(nameT[0]);

                    changeStatus(order);

                    // Tính tổng giá của topping
                    long totalToppingPrice = 0;
                    for (OrderedProduct p : order.getOrderedProducts()) {
                        for (Map.Entry<String, Long> entry : p.getToppings().entrySet()) {
                            Topping t = new Topping();
                            t.setName(entry.getKey());
                            t.setPrice(entry.getValue());
                            totalToppingPrice += t.getPrice();
                            Log.d("Topping price", "onCallback: " + t.getPrice());
                        }
                    }

                    // Tính tổng giá
                    long totalPrice = 0;
                    for (OrderedProduct orderedProduct : order.getOrderedProducts()) {
                        totalPrice += orderedProduct.getAmount() * orderedProduct.getProduct().getPrice();
                    }
                    totalPrice += totalToppingPrice;
                    totalPrice += vanChuyen;
                    binding.giaDonHang.setText((totalPrice - vanChuyen - totalToppingPrice) +"");
                    binding.tongDonHang.setText(totalPrice+"");

                    manager = new GridLayoutManager(PayingActivity.this, 1);
                    manager.setOrientation(GridLayoutManager.HORIZONTAL);
                    orderAdapter = new OrderAdapter(PayingActivity.this, order, true);

                    if (binding.phuongThucThanhToan.equals("Thanh toán bằng Ngân hàng")) {
                        long finalTotalPrice = totalPrice;
                        PaymentAPI.find(PaymentAPI.SACOMBANK_KEY, new PaymentAPI.FirebaseCallback() {
                            @Override
                            public void onCallback(Payment payment) {
                                if (payment != null) {
                                    payment.setTotal(finalTotalPrice);
                                    ImageView qr = new ImageView(PayingActivity.this);
                                    ImageStorageReference.setImageInto(qr, payment.getQr());
                                }
                            }
                        });
                    } else if (binding.phuongThucThanhToan.equals("Thanh toán Momo")) {
                        long finalTotalPrice = totalPrice;
                        PaymentAPI.find(PaymentAPI.MOMO_KEY, new PaymentAPI.FirebaseCallback() {
                            @Override
                            public void onCallback(Payment payment) {
                                if (payment != null) {
                                    payment.setTotal(finalTotalPrice);
                                    ImageView qr = new ImageView(PayingActivity.this);
                                    ImageStorageReference.setImageInto(qr, payment.getQr());
                                }
                            }
                        });
                    }

                } else {
                    Toast.makeText(PayingActivity.this, "Lỗi tải thông tin đánh giá.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void changeStatus(Order order){
        switch (order.getStatus()){
            case 0:
                binding.statusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        order.setStatus(3);
                        Intent intent = new Intent(PayingActivity.this, OrderActivity.class);
                        startActivity(intent);
                    }
                });
                binding.statusOrder.setBackgroundResource(R.color.waiting);
                binding.statusOrder.setText("Đang chờ duyệt");
                break;
            case 1:
                binding.statusButton.setEnabled(false);
                binding.statusOrder.setBackgroundResource(R.color.accept);
                binding.statusOrder.setText("Đơn đã được xác nhận, vui lòng đợi");
                break;
            case 3:
                binding.statusButton.setEnabled(false);
                binding.statusOrder.setBackgroundResource(R.color.active_button);
                binding.statusOrder.setText("Đã hủy");
                break;
        }
    }
}