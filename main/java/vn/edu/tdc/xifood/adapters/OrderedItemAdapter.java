package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.databinding.OrderItemLayoutBinding;
import vn.edu.tdc.xifood.databinding.OrderedItemLayoutBinding;
import vn.edu.tdc.xifood.databinding.OrderedProductItemLayoutBinding;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.models.Order;
import vn.edu.tdc.xifood.datamodels.Product;

public class OrderedItemAdapter extends RecyclerView.Adapter<OrderedItemAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<OrderedProduct> products = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderedItemAdapter(Activity context, ArrayList<OrderedProduct> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(OrderedItemLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.textOrderCode.setText("Đơn số #"+position);
    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private OrderedItemLayoutBinding binding;

        public MyViewHolder(@NonNull OrderedItemLayoutBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(MyViewHolder viewHolder);
        public void onIncreaseAmount(MyViewHolder viewHolder);
        public void onDecreaseAmount(MyViewHolder viewHolder);
    }
}

