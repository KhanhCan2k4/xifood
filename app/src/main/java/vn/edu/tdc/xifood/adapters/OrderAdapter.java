package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.databinding.OrderItemLayoutBinding;
import vn.edu.tdc.xifood.models.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<Order> orders = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderAdapter(Activity context, ArrayList<Order> oders) {
        this.context = context;
        this.orders = oders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(OrderItemLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.binding.textOrderCode.setText(String.format("Đơn số #%d", order.getId()));

        GridLayoutManager manager = new GridLayoutManager(context, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);

        OrderedProductAdapter productAdapter = new OrderedProductAdapter(context, order.getProducts());
        holder.binding.orderList.setLayoutManager(manager);
        holder.binding.orderList.setAdapter(productAdapter);

        holder.binding.btnView.setText("Xem");
        holder.binding.btnBuyBack.setText("Mua lại");
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private OrderItemLayoutBinding binding;

        public MyViewHolder(@NonNull OrderItemLayoutBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(MyViewHolder viewHolder);
    }
}

