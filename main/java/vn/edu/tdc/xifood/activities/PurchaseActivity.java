package vn.edu.tdc.xifood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.OrderedProductAdapter;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.PurchaseLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.Order;
import vn.edu.tdc.xifood.mydatamodels.Order.OrderedProduct;
import vn.edu.tdc.xifood.mydatamodels.Product;
import vn.edu.tdc.xifood.mydatamodels.Review;
import vn.edu.tdc.xifood.mydatamodels.Topping;
import vn.edu.tdc.xifood.mydatamodels.User;
import vn.edu.tdc.xifood.views.ActionFooter;
import vn.edu.tdc.xifood.views.CancelHeader;

public class PurchaseActivity extends AppCompatActivity {
    private PurchaseLayoutBinding binding;
    private OrderedProductAdapter orderAdapter;
    private GridLayoutManager manager;
    private Order order;
    private String key;
    private Order thisOrder;
    private API<Order> orderAPI;
    private API<Topping> toppingAPI;
    private API<Product> productAPI;
    public static final String ORDERED_KEY = "ORDERED_KEY";
    public static final String IS_VIEW_MODE_KEY = "IS_VIEW_MODE_KEY";
    private boolean isViewMode;
    private boolean isStaff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PurchaseLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharePreference.setSharedPreferences(this);

        orderAPI = new API<>(Order.class, API.ORDER_TABLE_NAME);
        toppingAPI = new API<>(Topping.class, API.TOPPING_TABLE_NAME);
        productAPI = new API<>(Product.class, API.PRODUCT_TABLE_NAME);

        Intent intent = getIntent();
        key = intent.getStringExtra(ORDERED_KEY);
        isViewMode = intent.getBooleanExtra(IS_VIEW_MODE_KEY, false);

        //set title
        binding.cancelHeader.setTitle("Mua hàng");

        //get order's info
        order = new Order();
        ArrayList<OrderedProduct> orders = new ArrayList<>();
        manager = new GridLayoutManager(PurchaseActivity.this, 1);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        orderAdapter = new OrderedProductAdapter(PurchaseActivity.this, orders, true);
        binding.orderList.setLayoutManager(manager);
        binding.orderList.setAdapter(orderAdapter);

        orderAdapter.setTakeNoteListener(new OrderedProductAdapter.OnTakeNoteListener() {
            @Override
            public void onTakeNote(String note, int position) {
                if (position <= thisOrder.getOrderedProducts().size() - 1 && position >= 0) {
                    OrderedProduct orderedProduct = thisOrder.getOrderedProducts().get(position);
                    orderedProduct.setNote(note);

                    orderAPI.update(thisOrder, thisOrder.getKey(), null, null, null);
                }
            }

            //save user review
            @Override
            public void sendReview(String review, int rating, int position) {
                if (position <= thisOrder.getOrderedProducts().size() - 1 & !review.isEmpty()) {
                    OrderedProduct orderedProduct = thisOrder.getOrderedProducts().get(position);
                    orderedProduct.setReview(review);
                    orderedProduct.setRating(rating);

                    productAPI.find(orderedProduct.getKey(), new API.FirebaseCallback() {
                        @Override
                        public void onCallback(Object object) {
                            if (object != null) {
                                Product product = (Product) object;
                                float newRating = product.getRating() * (product.getBuyTimes() - orderedProduct.getQuantity()); //old rating
                                //is first rating
                                if (newRating < 1) {
                                    newRating = rating;
                                } else {
                                    newRating = (newRating + rating * orderedProduct.getQuantity()) / product.getBuyTimes();
                                }

                                product.setBuyTimes(product.getBuyTimes() + orderedProduct.getQuantity()); //new buy times
                                productAPI.update(product, product.getKey(), null, null, null);
                            }
                        }
                    }, null);

                    API<Review> reviewAPI = new API<>(Review.class, API.REVIEW_TABLE_NAME + "/" + orderedProduct.getKey());
                    Review newReview = new Review();
                    newReview.setContent(review);
                    newReview.setRating(rating);
                    newReview.setUserKey(SharePreference.find(SharePreference.USER_TOKEN_KEY));

                    reviewAPI.store(newReview, null, null, null);
                    orderAPI.update(thisOrder, thisOrder.getKey(), null, null, null);

                    new AlertDialog.Builder(PurchaseActivity.this)
                            .setMessage("Gửi đánh giá thành công")
                            .show();
                } else {
                    new AlertDialog.Builder(PurchaseActivity.this)
                            .setMessage("Gửi đánh giá không hợp lệ")
                            .show();
                }
            }
        });

        orderAPI.find(key, new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    thisOrder = (Order) object;
                    thisOrder.setKey(key);
                    isViewMode = !thisOrder.getStatus().equals("OD_01");
                    orderAdapter.setViewMode(isViewMode);
                    orderAdapter.setStaffMode(isStaff);
                    orders.clear();
                    orders.addAll(thisOrder.getOrderedProducts());
                    orderAdapter.notifyDataSetChanged();

                    //set price
                    OrderedProduct p = thisOrder.getOrderedProducts().get(0);
                    final long[] totalProduct = {0};
                    final long[] totalTopping = {0};
                    final long[] totalShip = {0};
                    productAPI.find(p.getKey(), new API.FirebaseCallback() {
                        @Override
                        public void onCallback(Object object) {
                            Product product = (Product) object;
                            totalProduct[0] = product.getPrice() * p.getQuantity();
                            binding.productsPrice.setText(Product.getPriceInFormat(totalProduct[0]));

                            final ArrayList<Topping>[] toppings = new ArrayList[]{new ArrayList<>()};
                            toppingAPI.all(new API.FirebaseCallbackAll() {
                                @Override
                                public void onCallback(ArrayList list) {

                                    toppings[0].clear();

                                    for (Object o : list) {
                                        Topping t = (Topping) o;
                                        if (p.getToppings().contains(t.getKey())) {
                                            totalTopping[0] += t.getPrice();
                                        }
                                    }
                                    binding.toppingsPrice.setText(Product.getPriceInFormat(totalTopping[0]));

                                    totalShip[0] = (totalProduct[0] + totalTopping[0]) * 10 / 100;
                                    if (totalShip[0] >= 30000) {
                                        totalShip[0] = 30000;
                                    }
                                    binding.shippingPrice.setText(Product.getPriceInFormat(totalShip[0]));
                                    binding.buyFooter.setPriceText(totalProduct[0] + totalTopping[0] + totalShip[0]);
                                }
                            });
                        }
                    });
                }
            }
        });

        // get all vouchers from firebase
        API<String> voucherAPI = new API<>(String.class, API.VOUCHER_TABLE_NAME);
        ArrayList<String> vouchers = new ArrayList<>();
        ArrayAdapter<String> adapterVoucher = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vouchers);
        binding.vocherList.setAdapter(adapterVoucher);

        voucherAPI.all(new API.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList list) {
                vouchers.addAll(list);
                adapterVoucher.notifyDataSetChanged();
            }
        });

        //get all payments from firebase
        ArrayList<String> payments = new ArrayList<>();
        payments.add("Tiền mặt");
        payments.add("Ví điện tử Momo (Đang cập nhật)");
        payments.add("Tài khoản ngân hàng (Đang cập nhật)");

        ArrayAdapter<String> adapterPayment = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, payments);
        binding.phuongThucThanhToan.setAdapter(adapterPayment);
        binding.phuongThucThanhToan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                binding.phuongThucThanhToan.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //set address
        binding.setAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseActivity.this, SetAddressActivity.class);
                startActivity(intent);
            }
        });

        binding.btnConfirmPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PurchaseActivity.this, SendOTPActivity.class);
                startActivity(intent);
            }
        });

        //get address
        String key = SharePreference.find(SharePreference.USER_TOKEN_KEY);
        API<User> userAPI = new API<>(User.class, API.USER_TABLE_NAME);

        binding.address.setTag(0);
        binding.phone.setTag(0);

        if (isViewMode) {
            binding.btnConfirmPhone.setVisibility(View.GONE);
        }

        userAPI.find(key, new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    User user = (User) object;

                    isStaff = user.getPermistion().equals("PM_03");

                    if (user.getAddress().size() > 0) { //have at least 1 address
                        binding.address.setText(user.getAddress().get(0));
                        binding.address.setTag(1);
                    }

                    if (!user.getPhoneNumber().isEmpty()) {
                        binding.phone.setText(user.getPhoneNumber());
                        binding.phone.setTag(1);
                    }
                }
            }
        });

        //events
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                if (isViewMode) {
                    finish();
                    return;
                }
                //confirm
                new AlertDialog.Builder(PurchaseActivity.this)
                        .setIcon(R.drawable.order_icon)
                        .setTitle("Mua hàng")
                        .setMessage("Bạn có chắc chắn muốn hủy mua hàng?")
                        .setPositiveButton("Bỏ mua hàng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                orderAPI.destroy(key, null, null, null);
                                finish();
                            }
                        })
                        .setNegativeButton("Bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .show();
            }
        });

        if (isViewMode) {
            binding.buyFooter.setButtonText("Đã lưu hoá đơn");
        }
        binding.buyFooter.setActionListener(new ActionFooter.OnActionListener() {
            @Override
            public void onAction(View view) {
                if (isViewMode) {
                    screenShot(binding.getRoot());
                } else {
                    if (binding.address.getTag().equals(0)) {
                        new AlertDialog.Builder(PurchaseActivity.this)
                                .setIcon(R.drawable.order_icon)
                                .setTitle("Không thể mua hàng")
                                .setMessage("Bạn chưa chọn địa chỉ giao hàng")
                                .show();
                        return;
                    }

                    if (binding.phone.getTag().equals(0)) {
                        new AlertDialog.Builder(PurchaseActivity.this)
                                .setIcon(R.drawable.order_icon)
                                .setTitle("Không thể mua hàng")
                                .setMessage("Bạn chưa xác nhận số điện thoại")
                                .show();
                        return;
                    }

                    //confirm
                    new AlertDialog.Builder(PurchaseActivity.this)
                            .setIcon(R.drawable.order_icon)
                            .setTitle("Xác nhận mua hàng")
                            .setMessage("Bạn đã chắc chắn đúng, đủ thông tin và muốn yêu cầu đặt hàng?")
                            .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    thisOrder.setStatus("OD_02");
                                    thisOrder.setDate(LocalDateTime.now().toString());
                                    thisOrder.setAddress(binding.address.getText().toString().trim());

                                    //change in firebase
                                    orderAPI.update(thisOrder, thisOrder.getKey(),
                                            new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    new AlertDialog.Builder(PurchaseActivity.this)
                                                            .setIcon(R.drawable.order_icon)
                                                            .setTitle("Mua hàng")
                                                            .setMessage("Mua hàng thành công")
                                                            .show();
                                                }
                                            },
                                            new OnCanceledListener() {
                                                @Override
                                                public void onCanceled() {
                                                    new AlertDialog.Builder(PurchaseActivity.this)
                                                            .setIcon(R.drawable.order_icon)
                                                            .setTitle("Mua hàng")
                                                            .setMessage("Mua hàng thất bại. Đã xảy ra lỗi, vui lòng thử lại")
                                                            .show();
                                                }
                                            },
                                            new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    //update product's buy times
                                                    for (OrderedProduct od : thisOrder.getOrderedProducts()) {
                                                        productAPI.find(od.getKey(), new API.FirebaseCallback() {
                                                            @Override
                                                            public void onCallback(Object object) {
                                                                if (object != null) {
                                                                    Product product = (Product) object;
                                                                    product.setBuyTimes(product.getBuyTimes() + od.getQuantity()); //new buy times
                                                                    productAPI.update(product, product.getKey(), null, null, null);
                                                                }
                                                            }
                                                        }, null);
                                                    }

                                                    Intent intent = new Intent(PurchaseActivity.this, OrderActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                    );
                                }
                            })
                            .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    return;
                                }
                            })
                            .show();
                }
            }
        });
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}