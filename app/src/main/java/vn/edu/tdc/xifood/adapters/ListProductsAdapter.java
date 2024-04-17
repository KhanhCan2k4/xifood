package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.databinding.ListProductBinding;
import vn.edu.tdc.xifood.models.Product;

public class ListProductsAdapter extends RecyclerView.Adapter<ListProductsAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<Product> products;

    public ListProductsAdapter(Activity context, ArrayList<Product> products) {
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
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListProductBinding listProductBinding;
        public ViewHolder(@NonNull ListProductBinding itemView) {
            super(itemView.getRoot());
            listProductBinding = itemView;
        }
    }
}
