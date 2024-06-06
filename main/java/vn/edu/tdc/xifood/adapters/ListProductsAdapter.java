package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.databinding.ListProductBinding;
import vn.edu.tdc.xifood.mydatamodels.Product;

public class ListProductsAdapter extends RecyclerView.Adapter<ListProductsAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<Product> products;
    

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ListProductsAdapter(Activity context,ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ListProductBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.listProductBinding.productImage.setImageResource(R.drawable.milk_tea);
        holder.listProductBinding.productName.setText(product.getName());
        holder.listProductBinding.priceProduct.setText(Product.getPriceInFormat(product.getPrice()));
        holder.listProductBinding.views.setText(product.getViews()+"");
        float rating = Math.round( product.getRating() * 10) / 10;
        holder.listProductBinding.rating.setText(rating + "");
        holder.listProductBinding.buyTimes.setText(product.getBuyTimes()+"");
        holder.setProductKey(product.getKey());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private int ProductId;

        public String getProductKey() {
            return productKey;
        }

        public void setProductKey(String productKey) {
            this.productKey = productKey;
        }

        private String productKey;

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int productId) {
            ProductId = productId;
        }

        private ListProductBinding listProductBinding;
        public ViewHolder(@NonNull ListProductBinding itemView) {
            super(itemView.getRoot());
            listProductBinding = itemView;

            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null){
                        itemClickListener.onItemClick(ViewHolder.this);
                    }else{
                        new AlertDialog.Builder(context)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setMessage("Không thể chọn sản phẩm này")
                                .show();
                    }
                }
            });
        }
    }

    public interface ItemClickListener{
        public void onItemClick(ViewHolder holder);
    }
}
