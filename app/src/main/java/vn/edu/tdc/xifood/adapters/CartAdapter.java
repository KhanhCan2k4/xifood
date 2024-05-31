package vn.edu.tdc.xifood.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.edu.tdc.xifood.activities.DetailUpdateProductActivity;
import vn.edu.tdc.xifood.apis.CartAPI;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.databinding.CartItemBinding;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.datamodels.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<OrderedProduct> orderedProducts;
    private String userId;
    private long totalBill = 0;
    private static final int REQUEST_CODE_UPDATE_PRODUCT = 1;
    private OnTotalBillUpdateListener totalBillUpdateListener;
    private HashMap<String, Boolean> checkedItems = new HashMap<>();
    private ArrayList<OrderedProduct> checkedItemsPay = new ArrayList<>();
    private HashMap<String, Long> itemPrices = new HashMap<>();

    public CartAdapter(Activity context, ArrayList<OrderedProduct> orderedProducts, String userId) {
        this.context = context;
        this.orderedProducts = orderedProducts;
        this.userId = userId;

        for (OrderedProduct orderedProduct : orderedProducts) {
            String productKey = orderedProduct.getProduct().getKey();
            checkedItems.put(productKey, false);
            itemPrices.put(productKey, calculateTotalProductPrice(orderedProduct));
        }
    }

    public void setTotalBillUpdateListener(OnTotalBillUpdateListener listener) {
        this.totalBillUpdateListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CartItemBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderedProduct orderedProduct = orderedProducts.get(position);

        if (orderedProduct != null) {
            Product product = orderedProduct.getProduct();

            if (product != null) {
                holder.cartBinding.sttSanPham.setText("Đơn số #" + (position + 1));
                holder.cartBinding.tenSanPham.setText(product.getName());
                holder.cartBinding.soLuongSanPham.setText(String.valueOf(orderedProduct.getAmount()));

                Long price = product.getPrice();
                long totalPrice = (price != null) ? price * orderedProduct.getAmount() : 0;
                holder.cartBinding.giaSanPham.setText("Tổng Giá: " + totalPrice + "vnd");

                holder.cartBinding.noteSanPham.setText("Ghi chú: " + product.getDescription());
                ImageStorageReference.setImageInto(holder.cartBinding.hinhSanPham, product.getImage().get(0));

                StringBuilder toppingsText = new StringBuilder();
                Map<String, Long> toppings = orderedProduct.getToppings();

                for (Map.Entry<String, Long> entry : toppings.entrySet()) {
                    toppingsText.append(entry.getKey()).append(": ").append(entry.getValue() != null ? entry.getValue() : 0).append("vnd\n");
                }
                holder.cartBinding.toppingThem.setText(toppingsText.toString());

                final long[] totalProductPrice = {totalPrice};
                for (Map.Entry<String, Long> entry : toppings.entrySet()) {
                    if (entry.getValue() != null) {
                        totalProductPrice[0] += entry.getValue();
                    }
                }
                holder.cartBinding.giaSanPham.setText("Tổng giá: " + totalProductPrice[0] + "vnd");


                holder.cartBinding.checkChonMua.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    holder.isCheckEnabled = !isChecked;

                    holder.cartBinding.btnGiam.setEnabled(!isChecked);
                    holder.cartBinding.btnTang.setEnabled(!isChecked);
                    holder.cartBinding.btnsua.setEnabled(!isChecked);
                    holder.cartBinding.btnxoa.setEnabled(!isChecked);

                    final long totalProductPriceLocal = totalProductPrice[0];

                    checkedItems.put(product.getKey(), true);
                    orderedProduct.setCheckedPay(true);
                    updateFirebaseCheckPay(orderedProduct, isChecked);

                    if (isChecked) {
                        totalBill += totalProductPriceLocal;
                    } else {
                        totalBill -= totalProductPriceLocal;
                    }

                    if (totalBillUpdateListener != null) {
                        totalBillUpdateListener.onTotalBillUpdate(totalBill);
                    }
                });

                holder.cartBinding.checkChonMua.setChecked(false);

            } else {
                holder.cartBinding.tenSanPham.setText("Unknown Product");
                holder.cartBinding.giaSanPham.setText("Giá: N/A");
                holder.cartBinding.noteSanPham.setText("Ghi chú: N/A");
            }

            holder.cartBinding.btnxoa.setOnClickListener(v -> removeItemByKey(orderedProduct.getProduct().getKey()));

            holder.cartBinding.btnsua.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailUpdateProductActivity.class);
                intent.putExtra("PRODUCT_ID", orderedProduct.getProduct().getKey());
                intent.putExtra("AMOUNT", orderedProduct.getAmount());

                StringBuilder selectedToppingsString = new StringBuilder();
                for (Map.Entry<String, Long> entry : orderedProduct.getToppings().entrySet()) {
                    selectedToppingsString.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
                }
                String toppingsString = selectedToppingsString.toString();
                intent.putExtra("SELECTED_TOPPINGS", toppingsString);
                context.startActivityForResult(intent, REQUEST_CODE_UPDATE_PRODUCT);
            });
        } else {
            holder.cartBinding.sttSanPham.setText("Đơn số #" + (position + 1));
            holder.cartBinding.tenSanPham.setText("Unknown Product");
            holder.cartBinding.giaSanPham.setText("Giá: N/A");
            holder.cartBinding.noteSanPham.setText("Ghi chú: N/A");
        }

        holder.cartBinding.btnTang.setOnClickListener(v -> {
            int currentAmount = orderedProduct.getAmount();
            if (currentAmount > 1) {
                orderedProduct.setAmount(currentAmount - 1);
                holder.cartBinding.soLuongSanPham.setText(String.valueOf(orderedProduct.getAmount()));
                notifyItemChanged(position);
                updatePrice(holder, orderedProduct);
            }
        });

        holder.cartBinding.btnGiam.setOnClickListener(v -> {
            int currentAmount = orderedProduct.getAmount();
            if (currentAmount < 5) {
                orderedProduct.setAmount(currentAmount + 1);
                holder.cartBinding.soLuongSanPham.setText(String.valueOf(orderedProduct.getAmount()));
                notifyItemChanged(position);
                updatePrice(holder, orderedProduct);
            }
        });
    }
    private long calculateTotalProductPrice(OrderedProduct orderedProduct) {
        long totalPrice = orderedProduct.getProduct().getPrice() * orderedProduct.getAmount();
        Map<String, Long> toppings = orderedProduct.getToppings();
        for (Map.Entry<String, Long> entry : toppings.entrySet()) {
            if (entry.getValue() != null) {
                totalPrice += entry.getValue();
            }
        }
        return totalPrice;
    }

    public void removeItemByKey(String productKey) {
        for (int i = 0; i < orderedProducts.size(); i++) {
            OrderedProduct orderedProduct = orderedProducts.get(i);
            if (orderedProduct.getProduct().getKey().equals(productKey)) {
                orderedProducts.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, orderedProducts.size()); // Update the index of items after removing

                // Remove the item from Firebase
                CartAPI.destroy(userId, productKey, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("CartAdapter", "Item removed successfully from Firebase.");
                    }
                }, new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("CartAdapter", "Failed to remove item from Firebase.");
                    }
                });
                return;
            }
        }
    }

    private void updatePrice(ViewHolder holder, OrderedProduct orderedProduct) {
        if (orderedProduct == null || orderedProduct.getProduct() == null) {
            Log.e("CartAdapter", "OrderedProduct or Product is null.");
            return;
        }

        Long priceObj = orderedProduct.getProduct().getPrice();
        if (priceObj == null) {
            Log.e("CartAdapter", "Price of the Product is null.");
            holder.cartBinding.giaSanPham.setText("Tổng giá: N/A");
            return;
        }

        long productPrice = priceObj * orderedProduct.getAmount();
        long toppingPrice = 0;
        Map<String, Long> toppings = orderedProduct.getToppings();

        if (toppings != null) {
            for (Map.Entry<String, Long> entry : toppings.entrySet()) {
                if (entry.getValue() != null) {
                    toppingPrice += entry.getValue();
                }
            }
        } else {
            Log.e("CartAdapter", "Toppings map is null.");
        }

        long totalPrice = productPrice + toppingPrice;
        holder.cartBinding.giaSanPham.setText("Tổng giá: " + totalPrice + "vnd");

        String productKey = orderedProduct.getProduct().getKey();
        boolean isChecked = checkedItems.getOrDefault(productKey, false);

        if (isChecked) {
            totalBill = 0; // Reset totalBill
            for (OrderedProduct product : orderedProducts) {
                long productTotalPrice = calculateTotalProductPrice(product);
                totalBill += productTotalPrice;
            }
            Log.d("CartAdapter", "Total Bill: " + totalBill);
            if (totalBillUpdateListener != null) {
                totalBillUpdateListener.onTotalBillUpdate(totalBill);
            }
        }

        itemPrices.put(productKey, totalPrice);
        Intent intent = new Intent();
        intent.putExtra("totalBill", totalBill);
        updateFirebase(orderedProduct);
    }


    @Override
    public int getItemCount() {
        return orderedProducts.size();
    }

    private void updateFirebaseCheckPay(OrderedProduct orderedProduct, boolean isChecked) {
        orderedProduct.setCheckedPay(isChecked);
        CartAPI.update(userId, orderedProduct, new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("CartAdapter", "Update thành công trạng thái isChecked của sản phẩm trong giỏ hàng.");
            }
        }, new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e("CartAdapter", "Cập nhật trạng thái isChecked của sản phẩm trong giỏ hàng bị hủy.");
            }
        });
    }

    public ArrayList<OrderedProduct> getCheckedItems() {
        ArrayList<OrderedProduct> checkedItemsList = new ArrayList<>();
        for (OrderedProduct orderedProduct : orderedProducts) {
            Boolean isChecked = checkedItems.getOrDefault(orderedProduct.getProduct().getKey(), false);
            if (isChecked) {
                checkedItemsList.add(orderedProduct);
            }
        }
        return checkedItemsList;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CartItemBinding cartBinding;
        boolean isCheckEnabled = true;

        public ViewHolder(@NonNull CartItemBinding binding) {
            super(binding.getRoot());
            this.cartBinding = binding;
        }
    }
    private void updateFirebase(OrderedProduct orderedProduct) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cartRef = database.getReference("carts").child(userId).child(orderedProduct.getProduct().getKey());

        cartRef.setValue(orderedProduct)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("CartAdapter", "Cập nhật thành công.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CartAdapter", "Cập nhật thất bại: " + e.getMessage());
                    }
                });
    }


    public interface OnTotalBillUpdateListener {
        void onTotalBillUpdate(long totalBill);
    }
}