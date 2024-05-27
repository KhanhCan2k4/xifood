package vn.edu.tdc.xifood.activities;

import androidx.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.apis.PaymentAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.ToppingAPI;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.databinding.PurchaseLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.datamodels.Payment;
import vn.edu.tdc.xifood.datamodels.Topping;
import vn.edu.tdc.xifood.datamodels.User;
import vn.edu.tdc.xifood.views.CancelHeader;

public class PurchaseActivity extends AppCompatActivity {
    private PurchaseLayoutBinding binding;
    private OrderAdapter orderAdapter;
    private GridLayoutManager manager;
    private String key;
    public static final int REQUEST_CODE_SET_ADDRESS = 123; // Thay 123 bằng giá trị bạn muốn
    private Order thisOrder;
    private Boolean xacNhanSDT = true;

    public static final String ORDERED_KEY = "ORDERED_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PurchaseLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharePreference.setSharedPreferences(this);

        Intent intent = getIntent();
        key = intent.getStringExtra(ORDERED_KEY);
        Spinner vocherList = binding.vocherList;
        Spinner phuongThucThanhToan = binding.phuongThucThanhToan;
        //khai bao cac gia tri
        TextView giaSanPham = binding.giaProduct;
        TextView giaTopping = binding.giaTopping;
        TextView phiVanChuyen = binding.phiVanChuyen;
        ImageButton setAddress = binding.setaddress;
        TextView giaVoCher = binding.giaGia;
        TextView tongDonHang = binding.tongDonHang;
        TextView diaChi = binding.diaChi;
        TextView tenVoucher = binding.voucherName;
        EditText ghichu = binding.ghiChu;
        Button buy = binding.buy;
        final long[] tongGia = {0};
        final long[] giaProduct = {0};
        final long[] tienTopping = {0};
        long vanchuyen = 10000;
        final double[] vocher = {0};
        final long[] sotienduocgiam = {0};
        final long[] ketqaucuoicung = {0};
        final String[] note = {""};

        //chuan  bi du lieu gia
        // Chuẩn bị dữ liệu
        String[] vochers = {"Không voucher"};
        ArrayAdapter<String> adapterVoucher = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vochers);
        adapterVoucher.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vocherList.setAdapter(adapterVoucher);

        String[] adapterPhuongThucThanhToans = {"Thanh toán khi nhận hàng", "Thanh toán Momo", "Thanh toán bằng Ngân Hàng"};
        ArrayAdapter<String> adapterPhuongThucThanhToan = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, adapterPhuongThucThanhToans);
        adapterVoucher.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phuongThucThanhToan.setAdapter(adapterPhuongThucThanhToan);




        OrderAPI.find(key, new OrderAPI.FirebaseCallback() {
            @Override
            public void onCallback(Order order) {
                thisOrder = order;
                vocherList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        tenVoucher.setText(vochers[position]);
                        if (vochers[position].equals("Không voucher")) {
                            vocher[0] = 0;
                        }
                        if (vochers[position].equals("Mừng khai trương")) {
                            vocher[0] = 0.5;
                        }
                        if (vochers[position].equals("Voucher mỗi tháng")) {
                            vocher[0] = 0.1;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                if (order != null) {

                    order.setNote(String.valueOf(ghichu.getText()));
                    OrderAPI.update(order);
                    ///////adress để qua 1 bên
                    /////THỰC HIỆN VIỆC XÁC NHẬN ĐIA CHỈ

                    if (order.getUser().getAddress() != null ) {
                        if (order.getUser().getAddress() != null && !order.getUser().getAddress().isEmpty()){
                            diaChi.setText(order.getUser().getAddress().get(0) + "");
                        }
                        else {
                            diaChi.setText("Cập nhật địa chỉ tại biểu tượng địa chỉ ở bên trái");
                        }
                    } else {
                        diaChi.setText("Cập nhật địa chỉ tại biểu tượng địa chỉ ở bên trái");
                    }
                    setAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PurchaseActivity.this, SetAddressActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            order.getUser().setAddress(new ArrayList<>());
                            startActivity(intent);

                        }
                    });

                    // tinh toan gia tien
                    ArrayList<OrderedProduct> listOderProduct = new ArrayList<>();

                    listOderProduct = order.getOrderedProducts();
                    Log.d("THANHTOAN_soluongsanpham", " " + listOderProduct.size());

                    for (OrderedProduct p : listOderProduct) {

                        giaProduct[0] += (p.getProduct().getPrice() * p.getAmount());
                        Log.d("giaproduct", giaProduct[0] + " ");

                        ToppingAPI.all(new ToppingAPI.FirebaseCallbackAll() {
                            @Override
                            public void onCallback(ArrayList<Topping> toppings) {
                                for (Topping t : toppings) {
                                    Log.d("tentopping", "onCallback: " + t.getName());
                                    if (p.getToppings().get(t.getName()) != null) {
                                        Log.d("hienthitientopping", t.getName() + ":" + p.getToppings().get(t.getName()) + " ");
                                        tienTopping[0] += t.getPrice();
                                        Log.d("giatientopping", "onCallback: " + tienTopping[0]);
                                    }
                                }
                                // Sau khi hoàn thành việc tính giá topping, cập nhật tổng giá
                                tongGia[0] = giaProduct[0] + tienTopping[0];
                                sotienduocgiam[0] = soTienGiam(tongGia[0], vocher[0]);
                                ketqaucuoicung[0] = tongGia[0] + vanchuyen - sotienduocgiam[0];
                                giaSanPham.setText(String.valueOf(giaProduct[0]));
                                giaTopping.setText(String.valueOf(tienTopping[0]));
                                phiVanChuyen.setText(String.valueOf(vanchuyen));
                                giaVoCher.setText(String.valueOf(sotienduocgiam[0]));
                                tongDonHang.setText(String.valueOf(ketqaucuoicung[0]));

                                phuongThucThanhToan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (position){
                                            case 1:
                                                PaymentAPI.find(PaymentAPI.MOMO_KEY, new PaymentAPI.FirebaseCallback() {
                                                    @Override
                                                    public void onCallback(Payment payment) {
                                                        if (payment != null) {
                                                            payment.setTotal(ketqaucuoicung[0]);
                                                            showPaymentDialog(payment, "Thay toán ví điện tử", R.drawable.momo_logo, ketqaucuoicung[0]);
                                                            intent.putExtra("Payment", adapterPhuongThucThanhToans[position]);
                                                        }
                                                    }
                                                });
                                                break;
                                            case 2:
                                                PaymentAPI.find(PaymentAPI.SACOMBANK_KEY, new PaymentAPI.FirebaseCallback() {
                                                    @Override
                                                    public void onCallback(Payment payment) {
                                                        if (payment != null) {
                                                            payment.setTotal(ketqaucuoicung[0]);
                                                            showPaymentDialog(payment, "Thay toán qua ngân hàng", R.drawable.bank_logo, ketqaucuoicung[0]);
                                                        }
                                                    }
                                                });
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        });
                    }

                    manager = new GridLayoutManager(PurchaseActivity.this, 1);
                    manager.setOrientation(GridLayoutManager.HORIZONTAL);
                    orderAdapter = new OrderAdapter(PurchaseActivity.this, order, true);
                    orderAdapter.setPurchaseActivity(true);
                    binding.orderList.setLayoutManager(manager);
                    binding.orderList.setAdapter(orderAdapter);


                    buy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (xacNhanSDT) {
                                if (order.getUser().getAddress() != null && order.getUser().getAddress().size() > 0) {
                                    // Cập nhật ghi chú của đơn hàng
                                    order.setNote(String.valueOf(ghichu.getText()));
                                    // Cập nhật trạng thái của đơn hàng
                                    order.setStatus(Order.STATUS_WAITING);
                                    order.setPayment(String.valueOf(phuongThucThanhToan.getSelectedItem()));
                                    order.setVoucher(String.valueOf(vocherList.getSelectedItem()));
                                    // Cập nhật đơn hàng trong cơ sở dữ liệu
                                    OrderAPI.update(order);

                                    // Chuyển hướng sang OrderActivity
                                    Intent intent = new Intent(PurchaseActivity.this, OrderActivity.class);
                                    // Đặt đơn hàng vào intent để truyền sang OrderActivity
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Order", order); // Nhớ implement Serializable cho lớp Order
                                    intent.putExtras(bundle);
                                    intent.putExtra("Payment", String.valueOf(phuongThucThanhToan));
                                    intent.putExtra("Voucher", vochers);
                                    startActivity(intent);

                                    // Kết thúc hoạt động hiện tại (PurchaseActivity)
                                    finish();
                                } else {
                                    Toast.makeText(PurchaseActivity.this, "Vui lòng cập nhật địa chỉ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Xử lý xác nhận số điện thoại tại đây
                                Toast.makeText(PurchaseActivity.this, "Vui lòng xác nhận số điện thoại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(PurchaseActivity.this, "Thông tin đơn hàng lỗi, vui lòng thử lại :<", Toast.LENGTH_SHORT).show();
                    finish();
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

    public void showPaymentDialog(Payment payment, String title, int icon, long total) {
        ImageView qr = new ImageView(PurchaseActivity.this);
        try {
            ImageStorageReference.setImageInto(qr, payment.getQr());
        } catch (Exception e) {
            //ignore
        }
        payment.setTotal(total);

        AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseActivity.this);
        builder.setCancelable(false);
        builder.setIcon(icon);
        builder.setTitle(title);
        builder.setMessage(payment.toString());
        builder.setView(qr);
        builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Sao chép STK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(payment.getCode(), payment.getCode()));
                Toast.makeText(PurchaseActivity.this, "Đã sao chép", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String key = SharePreference.find(SharePreference.USER_TOKEN_KEY);
        TextView diaChi = binding.diaChi;
        UserAPI.find(key, new UserAPI.FirebaseCallback() {
            @Override
            public void onCallback(User user) {
                if (thisOrder!=null &&  user != null && user.getAddress() != null && user.getAddress().size() > 0) {
                    // if(user.getAddress().get(0).equals("a")==false) {
                    diaChi.setText(user.getAddress().get(0) + "");
                    thisOrder.setUser(user);
                    thisOrder.setAddress(user.getAddress().get(0));
                    OrderAPI.update(thisOrder);
                    // }
                } else {
                    diaChi.setText("Cập nhật địa chỉ tại biểu tượng địa chỉ ở bên trái");
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SET_ADDRESS && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("selectedAddress")) {
                String selectedAddress = data.getStringExtra("selectedAddress");
                // Cập nhật địa chỉ trong đơn đặt hàng
                thisOrder.setAddress(selectedAddress);
                // Hiển thị địa chỉ đã chọn lên giao diện
                binding.diaChi.setText(selectedAddress);
            }
        }
    }


    private long soTienGiam(long tongGia, double vochern) {
        return (long) (tongGia * vochern);
    }

}
