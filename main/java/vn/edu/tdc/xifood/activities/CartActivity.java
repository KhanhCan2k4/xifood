package vn.edu.tdc.xifood.activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.CartAdapter;
import vn.edu.tdc.xifood.ImageStorageReference;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.ActivityCartLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.Cart;
import vn.edu.tdc.xifood.mydatamodels.Order;
import vn.edu.tdc.xifood.mydatamodels.Payment;
import vn.edu.tdc.xifood.mydatamodels.Table;
import vn.edu.tdc.xifood.mydatamodels.User;
import vn.edu.tdc.xifood.views.ActionFooter;
import vn.edu.tdc.xifood.views.CancelHeader;

public class CartActivity extends AppCompatActivity {

    private ActivityCartLayoutBinding binding;
    private Set<Cart.InCartProduct> selectedItems;
    private CartAdapter adapter;
    private API<Cart> cartAPI;
    private Cart cart;
    private String userKey;
    private long totalPrice;
    private ArrayList<Table> tableArrayList;
    private boolean isStaff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        totalPrice = 0L;
        selectedItems = new HashSet<>();

        // Initialize SharedPreferences
        SharePreference.setSharedPreferences(this);
        cartAPI = new API<>(Cart.class, API.CART_TABLE_NAME);
        userKey = SharePreference.find(SharePreference.USER_TOKEN_KEY);

        new API<User>(User.class, API.USER_TABLE_NAME).find(userKey, new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    User user = (User) object;
                    isStaff = user.getPermistion().equals("PM_03");
                    if (!isStaff) {
                        binding.tableTitle.setVisibility(View.GONE);
                        binding.tables.setVisibility(View.GONE);
                        binding.payments.setVisibility(View.GONE);
                    }
                }
            }
        });

        tableArrayList = new ArrayList<>();
        final ArrayAdapter<Table> tableArrayAdapter = new ArrayAdapter<>(CartActivity.this, android.R.layout.simple_spinner_item, tableArrayList);
        binding.tables.setAdapter(tableArrayAdapter);

        new API<Table>(Table.class, API.TABLE_TABLE_NAME).all(new API.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList list) {
                tableArrayList.clear();

                for (Object o : list) {
                    Table table = (Table) o;

                    if (table.getStatus().equals("TB_01")) {
                        tableArrayList.add(table);
                    }
                }

                final ArrayAdapter<Table> tableArrayAdapter = new ArrayAdapter<>(CartActivity.this, android.R.layout.simple_spinner_item, tableArrayList);
                binding.tables.setAdapter(tableArrayAdapter);
                tableArrayAdapter.notifyDataSetChanged();
            }
        });

        cart = new Cart();

        // Initialize adapter and LinearLayoutManager
        cart = new Cart();
        adapter = new CartAdapter(this, cart);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listCart.setLayoutManager(manager);
        binding.listCart.setAdapter(adapter);

        adapter.setCheckProductListener(new CartAdapter.CheckProductListener() {
            @Override
            public void onCheckProduct(int position, boolean isCheck) {
                //check and save to list
                if (isCheck) {
                    totalPrice += adapter.getTotalPrice(position);
                    selectedItems.add(cart.getProducts().get(position));
                } else {
                    totalPrice -= adapter.getTotalPrice(position);
                    selectedItems.remove(cart.getProducts().get(position));
                }
                binding.cartFooter.setPriceText(totalPrice);
            }

            @Override
            public void onDeleteProduct(Cart.InCartProduct inCartProduct) {
                String key = inCartProduct.getKey();
                selectedItems.remove(key);
                cart.getProducts().remove(inCartProduct);

                cartAPI.update(cart, cart.getUserKey(),
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                new AlertDialog.Builder(CartActivity.this)
                                        .setIcon(R.drawable.cart_icon)
                                        .setTitle("Xoá sản phẩm")
                                        .setMessage("Xoá sản phẩm khỏi giỏ hàng thành công")
                                        .show();
                            }
                        },
                        new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                new AlertDialog.Builder(CartActivity.this)
                                        .setIcon(R.drawable.cart_icon)
                                        .setTitle("Xoá sản phẩm")
                                        .setMessage("Chưa thể xoá sản phẩm khỏi giỏ hàng")
                                        .show();
                            }
                        },
                        null
                );
            }

            @Override
            public void onEditProduct(Cart.InCartProduct inCartProduct) {
                Intent intent = new Intent(CartActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.DETAIL_PRODUCT_KEY, inCartProduct.getKey());
                intent.putExtra(DetailActivity.IN_CART_PRODUCT, inCartProduct);

                startActivity(intent);
            }
        });

        cartAPI.find(userKey, new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    cart = (Cart) object;
                    adapter.setCart(cart);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        binding.cancelHeader.setTitle("Giỏ hàng");
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });

        //show qr
        API<Payment> paymentAPI = new API<>(Payment.class, API.PAYMENT_TABLE_NAME);
        binding.btnBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentAPI.find("SACOMBANK_KEY", new API.FirebaseCallback() {
                    @Override
                    public void onCallback(Object object) {
                        if (object != null) {
                            Payment payment = (Payment) object;
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
                paymentAPI.find("MOMO_KEY", new API.FirebaseCallback() {
                    @Override
                    public void onCallback(Object object) {
                        if (object != null) {
                            Payment payment = (Payment) object;
                            payment.setTotal(totalPrice);
                            showPaymentDialog(payment, "Thay toán ví điện tử", R.drawable.momo_logo);
                        }
                    }
                });

            }
        });

        //buy
        binding.cartFooter.setActionListener(new ActionFooter.OnActionListener() {
            @Override
            public void onAction(View view) {
                //prepare an order
                Order order = new Order();
                ArrayList<Order.OrderedProduct> orderedProducts = new ArrayList<>();
                for (Cart.InCartProduct p : selectedItems) {
                    Order.OrderedProduct orderedProduct = new Order.OrderedProduct();
                    orderedProduct.setQuantity(p.getQuantity());
                    orderedProduct.setKey(p.getKey());
                    orderedProduct.setToppings(p.getToppings());
                    orderedProducts.add(orderedProduct);

                    cart.getProducts().remove(p);
                }
                order.setOrderedProducts(orderedProducts);
                if (isStaff) {
                    order.setStatus("OD_02"); //pending
                    order.setTable(binding.tables.getSelectedItem().toString());
                }

                API<Order> orderAPI = new API<>(Order.class, API.ORDER_TABLE_NAME);
                String key = orderAPI.store(order, null, null, null);

                new API<>(Cart.class, API.CART_TABLE_NAME).update(cart, cart.getUserKey(), null, null, null);

                Intent intent;
                if (isStaff) {
                    intent = new Intent(CartActivity.this, OrderActivity.class);
                } else {
                    order.setKey(key);
                    intent = new Intent(CartActivity.this, PurchaseActivity.class);
                    intent.putExtra(PurchaseActivity.ORDERED_KEY, key);
                }

                startActivity(intent);
                finish();
            }
        });
    }

    public void showPaymentDialog(Payment payment, String title, int icon) {
        ImageView qr = new ImageView(CartActivity.this);
        try {
            ImageStorageReference.setImageInto(qr, payment.getQr());
        } catch (Exception e) {
            //ignore
        }
        payment.setTotal(totalPrice);

        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
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
                Toast.makeText(CartActivity.this, "Đã sao chép", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}
