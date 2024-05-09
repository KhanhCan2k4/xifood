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
import vn.edu.tdc.xifood.datamodels.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<Order> orders = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private boolean isAnOrder = false;

    public boolean isAnOrder() {
        return isAnOrder;
    }

    public void setAnOrder(boolean anOrder) {
        isAnOrder = anOrder;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderAdapter(Activity context, ArrayList<Order> oders) {
        this.context = context;
        this.orders = oders;
    }

    public OrderAdapter(Activity context, Order oder, boolean isAnOrder) {
        this.context = context;
        this.orders.add(oder);
        this.isAnOrder = isAnOrder;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(OrderItemLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.binding.textOrderCode.setText(String.format("Đơn số #%d", position + 1));

        GridLayoutManager manager = new GridLayoutManager(context, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);

        OrderedProductAdapter productAdapter = new OrderedProductAdapter(context, order.getOrderedProducts());
        holder.binding.orderList.setLayoutManager(manager);
        holder.binding.orderList.setAdapter(productAdapter);

        holder.setKey(order.getKey());

        if (isAnOrder) {
            holder.binding.btnView.setVisibility(View.INVISIBLE);
            holder.binding.btnBuyBack.setVisibility(View.INVISIBLE);
        } else {
            holder.binding.btnView.setText("Xem");
            holder.binding.btnBuyBack.setText("Mua lại");
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private OrderItemLayoutBinding binding;
        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public MyViewHolder(@NonNull OrderItemLayoutBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;

            binding.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onView(binding.btnView, key);
                    }
                }
            });
            binding.btnBuyBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onBuyback(binding.btnBuyBack, key);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onView(View view, String key);
        public void onBuyback(View view, String key);
        public void onCancel(View view, String key);
    }
}

