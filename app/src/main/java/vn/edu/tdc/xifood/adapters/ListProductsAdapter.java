package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.databinding.ListProductBinding;
import vn.edu.tdc.xifood.datamodels.Product;

public class ListProductsAdapter extends RecyclerView.Adapter<ListProductsAdapter.ViewHolder> {
    private final Activity context;
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
        holder.listProductBinding.priceProduct.setText(product.getPrice() + "VND");
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

        private final ListProductBinding listProductBinding;
        public ViewHolder(@NonNull ListProductBinding itemView) {
            super(itemView.getRoot());
            listProductBinding = itemView;

            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("adapter", "Click");
                    if(itemClickListener != null){
                        itemClickListener.onItemClick(ViewHolder.this);
                    }else{
                        Log.d("adapter", "You must create an ItemClickLister before!!");
                    }
                }
            });
        }
    }

    public interface ItemClickListener{
        void onItemClick(ViewHolder holder);
    }
}
