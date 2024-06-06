package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.databinding.ItemSearchLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.Product;

public class ItemSearchAdapter extends RecyclerView.Adapter<ItemSearchAdapter.MyViewHolder> {
    private ArrayList<Product> products;
    private Activity context;
    private ItemClickListener itemClickListener;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // contructors
    public ItemSearchAdapter(ArrayList<Product> products, Activity context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemSearchLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = products.get(position);
        ItemSearchLayoutBinding binding = (ItemSearchLayoutBinding) holder.getBinding();
        binding.txtName.setText(product.getName());
        binding.btnImage.setImageResource(R.drawable.milk_tea);
        holder.key = product.getKey();
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ViewBinding binding;
        private String key;

        public String getKey() {
            return key;
        }

        public MyViewHolder(@NonNull ViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public ViewBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewBinding binding) {
            this.binding = binding;
        }
    }

    public interface ItemClickListener {
        public void onItemClick(MyViewHolder holder);
    }
}