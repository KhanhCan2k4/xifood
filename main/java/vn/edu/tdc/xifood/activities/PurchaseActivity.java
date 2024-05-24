package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.xifood.adapters.OrderAdapter;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.ToppingAPI;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.databinding.PurchaseLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.datamodels.Topping;
import vn.edu.tdc.xifood.datamodels.User;
import vn.edu.tdc.xifood.views.CancelHeader;

public class PurchaseActivity extends AppCompatActivity {
    private PurchaseLayoutBinding binding;
    private OrderAdapter orderAdapter;
    private GridLayoutManager manager;
    private String key;
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
        String[] vochers = {"không voucher"};
        ArrayAdapter<String> adapterVoucher = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vochers);
        adapterVoucher.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vocherList.setAdapter(adapterVoucher);

        String[] adapterPhuongThucThanhToans = {"Thanh toán bằng tiền mặt", "Thanh toán online"};
        ArrayAdapter<String> adapterPhuongThucThanhToan = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, adapterPhuongThucThanhToans);
        adapterVoucher.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phuongThucThanhToan.setAdapter(adapterPhuongThucThanhToan);


        OrderAPI.find(key, new OrderAPI.FirebaseCallback() {
            @Override
            public void onCallback(Order order) {
                thisOrder = order;
                Log.d("diachinguoidung", order.getUser().getAddress().size() + "");
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
                            Log.d("textnote", "onClick: " + order.getNote());
                            if (xacNhanSDT) {
//                                if (order.getUser().g tAddress() != null) {
//                                    note[0] = String.valueOf(ghichu.getText());
                                String s = "bạn chưa có địa chỉ!";
                                String s2="Chưa có địa chỉ nào!";
                                if (order.getUser().getAddress().size() > 0) {
                                    if( order.getUser().getAddress().get(0).equals(s)==false&&order.getUser().getAddress().get(0).equals(s2)==false){
                                        Log.d("so sanh diachi", order.getUser().getAddress().get(0));
                                    Log.d("so sanh", ""+order.getUser().getAddress().get(0).equals(s));
                                    String address = order.getUser().getAddress().get(0);
                                    order.setAddress(order.getUser().getAddress().get(0));
                                    order.setStatus(Order.STATUS_DONE);
                                    OrderAPI.update(order);
                                    Toast.makeText(PurchaseActivity.this, "Xin cảm ơn quý khách", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PurchaseActivity.this, OrderActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                    startActivity(intent);
                                    }else {
                                        Toast.makeText(PurchaseActivity.this, "Vui lòng cập nhật địa chỉ", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (order.getUser().getAddress().size() <= 0) {
                                    Toast.makeText(PurchaseActivity.this, "Vui lòng cập nhật địa chỉ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Xử lý xác nhận số đineje thoại tại đây
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

    private long soTienGiam(long tongGia, double vochern) {
        return (long) (tongGia * vochern);
    }


}
