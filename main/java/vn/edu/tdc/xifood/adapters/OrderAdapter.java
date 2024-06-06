package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.databinding.OrderItemLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<Order> orders = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private boolean isAnOrder = false;
    private boolean isPurchaseActivity = false;

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public boolean isAnOrder() {
        return isAnOrder;
    }

    public void setAnOrder(boolean anOrder) {
        isAnOrder = anOrder;
    }

    public void setPurchaseActivity(boolean purchaseActivity) {
        isPurchaseActivity = purchaseActivity;
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

        // hiển thị đơn hàng
        holder.binding.textOrderCode.setText(String.format("Đơn ngày %s %s", order.getDate().substring(0, 10), order.getDate().substring(11, 16)));

        GridLayoutManager manager = new GridLayoutManager(context, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);

        OrderedProductAdapter productAdapter = new OrderedProductAdapter(context, order.getOrderedProducts(), isPurchaseActivity);
        holder.binding.orderList.setLayoutManager(manager);
        holder.binding.orderList.setAdapter(productAdapter);

        holder.setKey(order.getKey());

        // trang thai
        if (isAnOrder) {
            holder.binding.btnView.setVisibility(View.INVISIBLE);
            holder.binding.btnBuyBack.setVisibility(View.INVISIBLE);
        } else {
            // để kiểm tra trạng thái là đã được đặt hay chưa -> xét lại nút
            switch (order.getStatus()) {
                case "OD_01":
                case "OD_02":
                    holder.binding.btnBuyBack.setText("Hủy");
                    holder.binding.btnBuyBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (itemClickListener != null) {
                                itemClickListener.onCancel(holder.binding.btnBuyBack, order.getKey());
                            }
                        }
                    });
                    break;
                case "OD_03":
                case "OD_04":
                case "OD_05":
                    holder.binding.btnBuyBack.setText("Mua lại");
                    holder.binding.btnBuyBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (itemClickListener != null) {
                                itemClickListener.onBuyback(holder.binding.btnView, order.getKey());
                            }
                        }
                    });
                    break;
                default:
                    holder.binding.btnBuyBack.setVisibility(View.GONE);
                    break;
            }
            holder.binding.btnView.setText("Xem");
            holder.binding.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onView(holder.binding.btnView, order.getKey());
                    }
                }
            });

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
        }
    }

    public interface OnItemClickListener {
        public void onView(View view, String key);

        public void onBuyback(View view, String key);

        public void onCancel(View view, String key);
    }
}

