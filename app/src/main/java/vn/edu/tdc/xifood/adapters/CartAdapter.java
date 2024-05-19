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
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Map;

import vn.edu.tdc.xifood.activities.CartActivity;
import vn.edu.tdc.xifood.activities.DetailActivity;
import vn.edu.tdc.xifood.activities.DetailUpdateProductActivity;
import vn.edu.tdc.xifood.apis.CartAPI;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.databinding.CartItemtBinding;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.datamodels.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<OrderedProduct> orderedProducts;
    private String userId;
    final int REQUEST_CODE_UPDATE_PRODUCT = 1;

    public CartAdapter(Activity context, ArrayList<OrderedProduct> orderedProducts, String userId) {
        this.context = context;
        this.orderedProducts = orderedProducts;
        this.userId = userId;
    }

    public CartAdapter() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CartItemtBinding.inflate(context.getLayoutInflater(), parent, false));
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

                long totalPrice = product.getPrice() * orderedProduct.getAmount();
                holder.cartBinding.giaSanPham.setText("Giá: " + totalPrice + "vnd");

                holder.cartBinding.noteSanPham.setText("Ghi chú: " + product.getDescription());

                StringBuilder toppingsText = new StringBuilder();
                Map<String, Long> toppings = orderedProduct.getToppings();
                for (Map.Entry<String, Long> entry : toppings.entrySet()) {
                    toppingsText.append(entry.getKey()).append(": ").append(entry.getValue()).append("vnd\n");
                }
                holder.cartBinding.toppingThem.setText(toppingsText.toString());

                long totalProductPrice = totalPrice;
                for (Map.Entry<String, Long> entry : toppings.entrySet()) {
                    totalProductPrice += entry.getValue() * orderedProduct.getAmount();
                }
                holder.cartBinding.giaSanPham.setText("Tổng giá: " + totalProductPrice + "vnd");
            } else {
                Log.e("CartAdapter", "Product is null at position: " + position);
                holder.cartBinding.tenSanPham.setText("Unknown Product");
                holder.cartBinding.giaSanPham.setText("Giá: N/A");
                holder.cartBinding.noteSanPham.setText("Ghi chú: N/A");
            }

            holder.cartBinding.btnxoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productKey = orderedProduct.getProduct().getKey();
                    removeItemByKey(productKey);
                }
            });

            holder.cartBinding.btnsua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

//                    removeItemByKey(orderedProduct.getProduct().getKey());
                }
            });


        } else {
            Log.e("CartAdapter", "OrderedProduct is null at position: " + position);
            holder.cartBinding.sttSanPham.setText("Đơn số #" + (position + 1));
            holder.cartBinding.tenSanPham.setText("Unknown Product");
            holder.cartBinding.giaSanPham.setText("Giá: N/A");
            holder.cartBinding.noteSanPham.setText("Ghi chú: N/A");
        }

        holder.cartBinding.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAmount = orderedProduct.getAmount();
                if (currentAmount < 5) {
                    orderedProduct.setAmount(currentAmount + 1);
                    notifyItemChanged(position);
                    updateFirebase(orderedProduct); // Cập nhật dữ liệu trên Firebase
                }
            }
        });

        holder.cartBinding.btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAmount = orderedProduct.getAmount();
                if (currentAmount > 0) {
                    orderedProduct.setAmount(currentAmount - 1);
                    notifyItemChanged(position);
                    updateFirebase(orderedProduct); // Cập nhật dữ liệu trên Firebase
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderedProducts.size();
    }

    // Phương thức cập nhật dữ liệu trên Firebase
    private void updateFirebase(OrderedProduct orderedProduct) {
        CartAPI.update(userId, orderedProduct.getOrder());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CartItemtBinding cartBinding;

        public ViewHolder(@NonNull CartItemtBinding itemView) {
            super(itemView.getRoot());
            cartBinding = itemView;
        }
    }

    public void removeItemByKey(String productKey) {
        for (int i = 0; i < orderedProducts.size(); i++) {
            OrderedProduct orderedProduct = orderedProducts.get(i);
            if (orderedProduct.getProduct().getKey().equals(productKey)) {
                orderedProducts.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, orderedProducts.size()); // Cập nhật lại chỉ mục của các phần tử sau khi xóa

                // Xóa đơn hàng khỏi Firebase
                CartAPI.destroy(userId, orderedProduct.getProduct().getKey(), new OnSuccessListener<Void>() {
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

    public interface OnEditItemClickListener {

        void onEditItemClick(OrderedProduct orderedProduct, int position);
    }

}
