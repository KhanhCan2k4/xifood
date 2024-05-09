package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.xifood.databinding.ListSearchLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Product;

public class ListSearchProductsAdapter extends RecyclerView.Adapter<ListSearchProductsAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<Product> products;
    private ListSearchLayoutBinding searchLayoutBinding;
    private ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public ListSearchProductsAdapter(Activity context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ListSearchLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull ListSearchLayoutBinding itemView) {
            super(itemView.getRoot());
            searchLayoutBinding = itemView;

            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null){
                        itemClickListener.onItemClick(ViewHolder.this);
                    }else{
                        Log.d("TAG", "onClick: ");
                    }
                }
            });

        }
    }


    public interface ItemClickListener{
        public void onItemClick(ViewHolder holder);
    }
}

