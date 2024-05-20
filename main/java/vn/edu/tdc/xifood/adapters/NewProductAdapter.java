package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xfood.databinding.NewProductsBinding;
import vn.edu.tdc.xifood.models.NewProduct;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ViewHolder> {

    private Activity content;
    private ArrayList<NewProduct> newProducts;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(NewProductsBinding.inflate(content.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            NewProduct newProduct = newProducts.get(position);
            holder.newProductsBinding.photoNewProduct.setImageResource(newProduct.getPhoto());
            holder.newProductsBinding.nameNewProduct.setText(newProduct.getName());
            holder.newProductsBinding.priceNewProduct.setText(newProduct.getPrice());
    }

    @Override
    public int getItemCount() {
        return newProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private NewProductsBinding newProductsBinding;
        public ViewHolder(@NonNull NewProductsBinding itemView) {
            super(itemView.getRoot());
            newProductsBinding = itemView;
        }
    }
}
