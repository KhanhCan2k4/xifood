package vn.edu.tdc.xifood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.StaffOrderAdapter;
import vn.edu.tdc.xifood.apis.CartAPI;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.OrderAPI;
import vn.edu.tdc.xifood.apis.PaymentAPI;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.TableAPI;
import vn.edu.tdc.xifood.databinding.StaffOrderLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.datamodels.Payment;
import vn.edu.tdc.xifood.datamodels.Product;
import vn.edu.tdc.xifood.datamodels.Table;
import vn.edu.tdc.xifood.views.CancelHeader;

public class StaffOrderActivity extends AppCompatActivity {
    private StaffOrderLayoutBinding binding;
    private long totalPrice = 0;
    private ArrayList<Table> tableArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StaffOrderLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cancelHeader.setTitle("Đặt đơn tại quán");
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });

        binding.btnCancelOrder.setEnabled(false);
        binding.btnAcceptOrder.setEnabled(false);

        binding.total.setText("0VND");

        tableArrayList = new ArrayList<>();

        TableAPI.all(new TableAPI.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList<Table> tables) {
                tableArrayList = tables;
                final ArrayAdapter<Table> tableArrayAdapter = new ArrayAdapter<>(StaffOrderActivity.this, android.R.layout.simple_spinner_item, tableArrayList);
                binding.tables.setAdapter(tableArrayAdapter);
                tableArrayAdapter.notifyDataSetChanged();
            }
        });

        CartAPI.find(new CartAPI.FirebaseCallback() {
            @Override
            public void onCallback(Order order) {
                if (order != null && !SharePreference.find(SharePreference.CART_KEY).isEmpty()) { //have a cart
                    Log.d("TAG", "onCallback: cart key" + SharePreference.find(SharePreference.CART_KEY));
                    Log.d("TAG", "onCallback: order key: " + order.getKey());

                    StaffOrderAdapter adapter = new StaffOrderAdapter(StaffOrderActivity.this, order);
                    LinearLayoutManager manager = new LinearLayoutManager(StaffOrderActivity.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);

                    binding.orderedProducts.setLayoutManager(manager);
                    binding.orderedProducts.setAdapter(adapter);

                    binding.btnCancelOrder.setEnabled(true);
                    binding.btnAcceptOrder.setEnabled(true);

                    long total = 0;
                    if (order.getOrderedProducts() != null) {
                        for (OrderedProduct orderedProduct : order.getOrderedProducts()) {
                            total += orderedProduct.getProduct().getPrice() * orderedProduct.getAmount();
                        }
                        binding.total.setText(Product.getPriceInFormat(total));
                    }
                    totalPrice = total;

                    adapter.setItemInteract(new StaffOrderAdapter.ItemDelete() {
                        @Override
                        public void onDelete(StaffOrderAdapter.ViewHolder item, int position) {
                            new AlertDialog.Builder(StaffOrderActivity.this)
                                    .setTitle("Xoá sản phẩm")
                                    .setMessage("Xoá sản phẩm này?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            Toast.makeText(StaffOrderActivity.this, "Đã xoá sản phẩm", Toast.LENGTH_SHORT).show();

                                            order.getOrderedProducts().remove(position);
                                            adapter.setOrder(order);
                                            onUpdateTotal();
                                            adapter.notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton("Huỷ", null).show(); //ignore
                        }

                        @Override
                        public void onUpdateTotal() {
                            if (order.getOrderedProducts() != null) {
                                long total = 0;
                                for (OrderedProduct orderedProduct : order.getOrderedProducts()) {
                                    total += orderedProduct.getProduct().getPrice() * orderedProduct.getAmount();
                                }
                                totalPrice = total;
                                binding.total.setText(Product.getPriceInFormat(total));
                            }
                        }
                    });

                    //remove all products
                    binding.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(StaffOrderActivity.this)
                                    .setTitle("Xoá giỏ hàng")
                                    .setMessage("Xoá tất cả sản phẩm này?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            binding.btnCancelOrder.setText("Đang tải...");
                                            binding.btnCancelOrder.setEnabled(false);
                                            binding.btnAcceptOrder.setEnabled(false);

                                            CartAPI.destroy()
                                                    .addOnSuccessListener(new OnSuccessListener() {
                                                        @Override
                                                        public void onSuccess(Object o) {
                                                            Toast.makeText(StaffOrderActivity.this, "Đã xoá giỏ hàng", Toast.LENGTH_LONG).show();

                                                            //remove in local
                                                            SharePreference.destroy(SharePreference.CART_KEY);

                                                            //restart
                                                            Intent intent = new Intent(StaffOrderActivity.this, StaffOrderActivity.class);

                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    })
                                                    .addOnCanceledListener(new OnCanceledListener() {
                                                        @Override
                                                        public void onCanceled() {
                                                            Toast.makeText(StaffOrderActivity.this, "Không thể xoá giỏ hàng", Toast.LENGTH_LONG).show();

                                                            binding.btnCancelOrder.setText("Huỷ");
                                                            binding.btnCancelOrder.setEnabled(true);
                                                            binding.btnAcceptOrder.setEnabled(true);
                                                        }
                                                    });
                                        }
                                    })
                                    .setNegativeButton("Huỷ", null).show(); //ignore
                        }
                    });

                    //accept to be an order
                    binding.btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(StaffOrderActivity.this)
                                    .setTitle("Tạo đơn hàng")
                                    .setMessage("Đặt đơn này?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            binding.btnAcceptOrder.setText("Đang tải...");
                                            binding.btnCancelOrder.setEnabled(false);
                                            binding.btnAcceptOrder.setEnabled(false);

                                            //update chosen table
                                            order.setTable(binding.tables.getSelectedItem().toString());

                                            OrderAPI.store(order)
                                                    .addOnSuccessListener(new OnSuccessListener() {
                                                        @Override
                                                        public void onSuccess(Object o) {
                                                            Toast.makeText(StaffOrderActivity.this, "Tạo đơn thành công", Toast.LENGTH_LONG).show();

                                                            //remove cart
                                                            CartAPI.destroy();

                                                            //remove cart in local
                                                            SharePreference.destroy(SharePreference.CART_KEY);

                                                            //restart
                                                            Intent intent = new Intent(StaffOrderActivity.this, OrderActivity.class);

                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    })
                                                    .addOnCanceledListener(new OnCanceledListener() {
                                                        @Override
                                                        public void onCanceled() {
                                                            Toast.makeText(StaffOrderActivity.this, "Không thể tạo đơn", Toast.LENGTH_LONG).show();

                                                            binding.btnAcceptOrder.setText("Tạo đơn");
                                                            binding.btnCancelOrder.setEnabled(true);
                                                            binding.btnAcceptOrder.setEnabled(true);
                                                        }
                                                    });
                                        }
                                    })
                                    .setNegativeButton("Huỷ", null).show(); //ignore
                        }
                    });

                    //show qr
                    binding.btnBank.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PaymentAPI.find(PaymentAPI.SACOMBANK_KEY, new PaymentAPI.FirebaseCallback() {
                                @Override
                                public void onCallback(Payment payment) {
                                    if (payment != null) {
                                        payment.setTotal(totalPrice);
                                        showPaymentDialog(payment, "Thay toán qua ngân hàng", R.drawable.bank_logo);
                                    }
                                }
                            });

                        }
                    });

                    binding.btnMomo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PaymentAPI.find(PaymentAPI.MOMO_KEY, new PaymentAPI.FirebaseCallback() {
                                @Override
                                public void onCallback(Payment payment) {
                                    if (payment != null) {
                                        payment.setTotal(totalPrice);
                                        showPaymentDialog(payment, "Thay toán ví điện tử", R.drawable.momo_logo);
                                    }
                                }
                            });

                        }
                    });


                } else {
                    binding.btnCancelOrder.setEnabled(false);
                    binding.btnAcceptOrder.setEnabled(false);
                }
            }
        });

    }

    public void showPaymentDialog(Payment payment, String title, int icon) {
        ImageView qr = new ImageView(StaffOrderActivity.this);
        try {
            ImageStorageReference.setImageInto(qr, payment.getQr());
        } catch (Exception e) {
            //ignore
        }
        payment.setTotal(totalPrice);

        AlertDialog.Builder builder = new AlertDialog.Builder(StaffOrderActivity.this);
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
                Toast.makeText(StaffOrderActivity.this, "Đã sao chép", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}