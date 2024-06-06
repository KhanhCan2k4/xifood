package vn.edu.tdc.xifood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.adapters.ReviewAdapter;
import vn.edu.tdc.xifood.adapters.ToppinAdapter;
import vn.edu.tdc.xifood.ImageStorageReference;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.ProductDetailsLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.Cart;
import vn.edu.tdc.xifood.mydatamodels.Order;
import vn.edu.tdc.xifood.mydatamodels.Product;
import vn.edu.tdc.xifood.mydatamodels.Review;
import vn.edu.tdc.xifood.mydatamodels.Topping;
import vn.edu.tdc.xifood.mydatamodels.User;
import vn.edu.tdc.xifood.views.CancelHeader;

public class DetailActivity extends AppCompatActivity {

    private ProductDetailsLayoutBinding binding;
    private ToppinAdapter toppinAdapter;
    private ReviewAdapter reviewAdapter;
    private String key = "";
    private Product product;
    private Cart cart;
    private ArrayList<String> orderedToppings = new ArrayList<>();
    private ArrayList<Review> reviews;
    private int amount = 1;
    public static final String DETAIL_PRODUCT_KEY = "DETAIL_PRODUCT_KEY";
    public static final String IN_CART_PRODUCT = "IN_CART_PRODUCT";
    public static final int MAX_AMOUNT = 5;
    private static Cart.InCartProduct editingInCartProduct;
    private API<Product> productAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //declare binding
        binding = ProductDetailsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharePreference.setSharedPreferences(this);

        //get key from intent
        Intent intent = getIntent();
        key = intent.getStringExtra(DETAIL_PRODUCT_KEY);
        editingInCartProduct = (Cart.InCartProduct) intent.getSerializableExtra(IN_CART_PRODUCT);

        //config heaeder
        binding.cancelHeader.setTitle("Xem chi tiết sản phẩm ");
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });

        //set default value for product's info
        binding.productName.setText("Đang tải...");
        binding.productPrice.setText("Đang tải...");
        binding.productDes.setText("Đang tải...");

        //get product from firebase
        productAPI = new API<>(Product.class, API.PRODUCT_TABLE_NAME);

        productAPI.find(key, new API.FirebaseCallback() {
            @Override
            public void onCallback(Object p) {
                if (p != null) {
                    product = (Product) p;
                    binding.productName.setText(product.getName());
                    binding.productPrice.setText(Product.getPriceInFormat(product.getPrice()));
                    binding.productDes.setText(product.getDescription());
                    if (product.getImage().size() > 0) {
                        ImageStorageReference.setImageInto(binding.productImg, product.getImage().get(0));
                    }

                }
            }
        });

        //config topping list adapter
        toppinAdapter = new ToppinAdapter(this, new ArrayList<>());
        LinearLayoutManager manager = new LinearLayoutManager(DetailActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.toppingList.setLayoutManager(manager);
        binding.toppingList.setAdapter(toppinAdapter);

        //config review list apapter
        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(DetailActivity.this, reviews);
        LinearLayoutManager reviewManager = new LinearLayoutManager(DetailActivity.this);
        reviewManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.reviewList.setLayoutManager(reviewManager);
        binding.reviewList.setAdapter(reviewAdapter);

        //get review from database
        API<Review> reviewAPI = new API<>(Review.class, API.REVIEW_TABLE_NAME + "/" + key);
        reviewAPI.all(new API.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList list) {
                reviews = list;
                reviewAdapter.setReviews(reviews);
                reviewAdapter.notifyDataSetChanged();
            }
        });

        //get toppings from firebase
        API<Topping> toppingApi = new API<Topping>(Topping.class, API.TOPPING_TABLE_NAME);
        toppingApi.all(new API.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList toppings) {
                toppinAdapter.setToppings(toppings);
                toppinAdapter.notifyDataSetChanged();
            }
        });

        //save checked toppings
        toppinAdapter.setOnItemCheckedListener(new ToppinAdapter.OnItemCheckedListener() {
            @Override
            public void setItemChecked(Boolean isChecked, String key) {
                if (isChecked) {
                    orderedToppings.add(key);
                } else {
                    orderedToppings.remove(key);
                }
            }
        });

        //set amount
        if (editingInCartProduct != null) {
            binding.amount.setSelection(editingInCartProduct.getQuantity() - 1);
            toppinAdapter.setComparingList(editingInCartProduct.getToppings());
            orderedToppings = editingInCartProduct.getToppings();
            toppinAdapter.notifyDataSetChanged();

            binding.cancelHeader.setTitle("Chỉnh sửa giỏ hàng");
            binding.addToCart.setText("Chỉnh sửa");
            binding.buyNow.setVisibility(View.GONE);

            binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
                @Override
                public void onCancel(View view) {
                    //confirm
                    new android.app.AlertDialog.Builder(DetailActivity.this)
                            .setIcon(R.drawable.cart_icon)
                            .setTitle("Thoát chỉnh sửa đơn hàng")
                            .setMessage("Bạn muốn thoát khỏi chỉnh sửa và không lưu?")
                            .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setNegativeButton("Huỷ", null)
                            .show();

                }
            });
        }

        //get user's key as cart's key
        SharePreference.setSharedPreferences(DetailActivity.this);
        String userKey = SharePreference.find(SharePreference.USER_TOKEN_KEY);

        //check permission
        new API<User>(User.class, API.USER_TABLE_NAME).find(userKey, new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    User user = (User) object;
                    if (user.getPermistion().equals("PM_03")) {
                        binding.buyNow.setVisibility(View.GONE);
                    }
                }
            }
        });

        API<Cart> cartAPI = new API<>(Cart.class, API.CART_TABLE_NAME);
        API<Order> orderAPI = new API<>(Order.class, API.ORDER_TABLE_NAME);

        //find cart
        cart = new Cart();
        cartAPI.find(userKey, new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    cart = (Cart) object;
                }
            }
        });

        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addToCart.setText("Đang tải...");
                binding.addToCart.setEnabled(false);

                //prepare product
                Cart.InCartProduct inCartProduct = new Cart.InCartProduct();
                inCartProduct.setKey(product.getKey());
                inCartProduct.setToppings(orderedToppings);

                int quantity;
                try {
                    quantity = Integer.parseInt(binding.amount.getSelectedItem().toString());
                } catch (Exception e) {
                    quantity = 1;
                }
                inCartProduct.setQuantity(quantity);

                //add new product -> update quantity
                for (Cart.InCartProduct p : cart.getProducts()) {
                    if (p.getKey().equals(inCartProduct.getKey())) {
                        if (editingInCartProduct == null) {
                            int newQuantity = p.getQuantity() + inCartProduct.getQuantity();
                            inCartProduct.setQuantity(newQuantity > MAX_AMOUNT ? MAX_AMOUNT : newQuantity);
                        }

                        cart.getProducts().remove(p);
                        break;
                    }
                }

                cart.getProducts().add(0, inCartProduct);
                storeToCart(cartAPI);

                //back to cart
                if (editingInCartProduct != null) {
                    finish();
                }
            }
        });
        binding.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buyNow.setText("Đang tải...");
                binding.buyNow.setEnabled(false);

                Order order = new Order();

                Order.OrderedProduct orderedProduct = new Order.OrderedProduct();
                orderedProduct.setKey(product.getKey());
                orderedProduct.setToppings(orderedToppings);

                int quantity;
                try {
                    quantity = Integer.parseInt(binding.amount.getSelectedItem().toString());
                } catch (Exception e) {
                    quantity = 1;
                }
                orderedProduct.setQuantity(quantity);

                order.getOrderedProducts().add(orderedProduct);

                //save order
                String key = orderAPI.store(order, null, null, null);
                order.setKey(key);
                Intent intent = new Intent(DetailActivity.this, PurchaseActivity.class);
                intent.putExtra(PurchaseActivity.ORDERED_KEY, key);
                startActivity(intent);

                binding.buyNow.setText(R.string.buyNow);
                binding.buyNow.setEnabled(true);
            }
        });
    }

    private void storeToCart(API<Cart> cartAPI) {
        cartAPI.update(cart, cart.getUserKey(),
                new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        //show user alert that user has just created a cart
                        showAlert("Thê sản phẩm vào giỏ hàng", "Thêm sản phẩm vào giỏ hàng thành công");
                    }
                },
                new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        //show alert
                        showAlert("Thê sản phẩm vào giỏ hàng", "Thêm sản phẩm vào giỏ hàng không thành công :<");
                    }
                },
                new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        //ignore
                        binding.addToCart.setText(R.string.addToCart);
                        binding.addToCart.setEnabled(true);
                    }
                });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.cart_icon)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (editingInCartProduct == null) {
            product.setViews(product.getViews() + 1);
            //update product's  view
            productAPI.update(product, product.getKey(), null, null, null);
        }
    }
}

