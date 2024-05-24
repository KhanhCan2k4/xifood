package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.databinding.ListItemRecentsSettingBinding;

public class RecentsProductsAdapter extends RecyclerView.Adapter<RecentsProductsAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<Product> list;
    private ItemClickListener itemClickListener;
    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // Constractors
    public RecentsProductsAdapter(Activity context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Lay du lieu tai position tu data src
        Product product = list.get(position);
        holder.binding.nameProducts.setText(product.getNameProduct());
        String price = product.getPriceProduct()+"";
        String[] result = price.split("000");
        holder.binding.priceProducts.setText(result[0]+".000đ");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ListItemRecentsSettingBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout itemlayout;
        private ListItemRecentsSettingBinding binding;

        public MyViewHolder(@NonNull ListItemRecentsSettingBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
    // Dinh nghia interface de uy quyen cho phia nguoi dung
    public interface ItemClickListener {
        public void onItemClick(MyViewHolder viewHolder);
    }

}
