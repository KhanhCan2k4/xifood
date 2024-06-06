package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.activities.DetailActivity;
import vn.edu.tdc.xifood.ImageStorageReference;
import vn.edu.tdc.xifood.databinding.CartItemBinding;
import vn.edu.tdc.xifood.mydatamodels.Cart;
import vn.edu.tdc.xifood.mydatamodels.Product;
import vn.edu.tdc.xifood.mydatamodels.Topping;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Activity context;
    private Cart cart;
    private CheckProductListener checkProductListener;
    private Product product;
    private Map<String, Long> totalPrices;

    private Map<String, Long> singlePrice;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public long getTotalPrice(int position) {
        String key = cart.getProducts().get(position).getKey();
        return totalPrices.get(key);
    }

    public CheckProductListener getCheckProductListener() {
        return checkProductListener;
    }

    public void setCheckProductListener(CheckProductListener checkProductListener) {
        this.checkProductListener = checkProductListener;
    }

    public CartAdapter(Activity context, Cart cart) {
        this.context = context;
        this.cart = cart;
        this.totalPrices = new HashMap<>();
        this.singlePrice = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CartItemBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int p = position;
        Cart.InCartProduct inCartProduct = cart.getProducts().get(p);
        totalPrices.put(inCartProduct.getKey(), 0L);
        singlePrice.put(inCartProduct.getKey(), 0L);

        API<Product> productAPI = new API<>(Product.class, API.PRODUCT_TABLE_NAME);
        product = new Product();

        productAPI.find(inCartProduct.getKey(), new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    product = (Product) object;
                    long price = totalPrices.get(inCartProduct.getKey());
                    price += product.getPrice() * inCartProduct.getQuantity();
                    totalPrices.put(inCartProduct.getKey(), price);

                    singlePrice.put(inCartProduct.getKey(), product.getPrice());

                    holder.cartBinding.tenSanPham.setText(product.getName());
                    holder.cartBinding.giaSanPham.setText(Product.getPriceInFormat(product.getPrice()));
                    holder.cartBinding.noteSanPham.setText(product.getDescription());
                    if (product.getImage().size() > 0) {
                        ImageStorageReference.setImageInto(holder.cartBinding.hinhSanPham, product.getImage().get(0));
                    }

                    holder.cartBinding.totalPrice.setText(Product.getPriceInFormat(totalPrices.get(inCartProduct.getKey())));
                }
            }
        });

        holder.cartBinding.soLuongSanPham.setText(String.valueOf(inCartProduct.getQuantity()));

        API<Topping> toppingAPI = new API<>(Topping.class, API.TOPPING_TABLE_NAME);
        toppingAPI.all(new API.FirebaseCallbackAll() {
            @Override
            public void onCallback(ArrayList list) {
                String toppingsText = "";

                long price = totalPrices.get(inCartProduct.getKey());
                for (Object o : list) {
                    Topping topping = (Topping) o;

                    if (inCartProduct.getToppings().contains(topping.getKey())) {
                        price += topping.getPrice();

                        toppingsText += String.format("\t%s\n", topping.getName());
                    }
                }
                holder.cartBinding.toppingThem.setText(toppingsText);
                holder.cartBinding.totalPrice.setText(Product.getPriceInFormat(price));
                totalPrices.put(inCartProduct.getKey(), price);
            }
        });

        holder.cartBinding.checkChonMua.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                holder.cartBinding.btnTang.setEnabled(!b);
                holder.cartBinding.btnGiam.setEnabled(!b);

                if (checkProductListener != null) {
                    checkProductListener.onCheckProduct(p, b);
                }
            }
        });

        long price = (Long) totalPrices.get(inCartProduct.getKey());
        holder.cartBinding.totalPrice.setText(Product.getPriceInFormat(price));

        holder.cartBinding.btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inCartProduct.getQuantity() < DetailActivity.MAX_AMOUNT) {
                    long price = totalPrices.get(inCartProduct.getKey());
                    price += singlePrice.get(inCartProduct.getKey());
                    totalPrices.put(inCartProduct.getKey(), price);
                    inCartProduct.setQuantity(inCartProduct.getQuantity() + 1);
                    holder.cartBinding.soLuongSanPham.setText(String.valueOf(inCartProduct.getQuantity()));
                    holder.cartBinding.totalPrice.setText(Product.getPriceInFormat(price));
                }
            }
        });

        holder.cartBinding.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inCartProduct.getQuantity() > 1) {
                    long price = totalPrices.get(inCartProduct.getKey());
                    price -= singlePrice.get(inCartProduct.getKey());
                    totalPrices.put(inCartProduct.getKey(), price);
                    inCartProduct.setQuantity(inCartProduct.getQuantity() - 1);
                    holder.cartBinding.soLuongSanPham.setText(String.valueOf(inCartProduct.getQuantity()));
                    holder.cartBinding.totalPrice.setText(Product.getPriceInFormat(price));
                }
            }
        });

        holder.cartBinding.btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //comfirm and remove
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.cart_icon)
                        .setTitle("Xoá sản phẩm")
                        .setMessage("Xác nhận xoá sản phẩm khỏi giỏ hàng?")
                        .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (checkProductListener != null) {
                                    checkProductListener.onDeleteProduct(inCartProduct);
                                }
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
        });

        holder.cartBinding.btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkProductListener != null) {
                    checkProductListener.onEditProduct(inCartProduct);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cart.getProducts().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CartItemBinding cartBinding;
        boolean isCheckEnabled = true;

        public ViewHolder(@NonNull CartItemBinding binding) {
            super(binding.getRoot());
            this.cartBinding = binding;
        }
    }

    public interface CheckProductListener {
        void onCheckProduct(int position, boolean isCheck);

        void onDeleteProduct(Cart.InCartProduct  inCartProduct);
        void onEditProduct(Cart.InCartProduct  inCartProduct);
    }
}