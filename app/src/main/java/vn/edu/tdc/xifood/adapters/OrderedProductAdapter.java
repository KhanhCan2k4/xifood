package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.databinding.OrderItemLayoutBinding;
import vn.edu.tdc.xifood.databinding.OrderedProductItemLayoutBinding;
import vn.edu.tdc.xifood.models.Order;
import vn.edu.tdc.xifood.models.Product;

public class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<Product> products = new ArrayList<>();
    private OnItemClickListener itemClickListener;

//    public OrderedProductAdapter(Activity context, ArrayList<Order.OrderedProduct> orderedProducts) {
//    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderedProductAdapter(Activity context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(OrderedProductItemLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = products.get(position);

        holder.binding.productName.setText(product.getName());
        holder.binding.productImage.setImageResource(R.drawable.ic_launcher_background);


        holder.binding.txtAmount.setText(product.getAmount() + "");

        holder.binding.btnDecreaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setAmount(product.getAmount() - 1);
                holder.binding.txtAmount.setText(product.getAmount() + "");
            }
        });

        holder.binding.btnIncreaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setAmount(product.getAmount() + 1);
                holder.binding.txtAmount.setText(product.getAmount() + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private OrderedProductItemLayoutBinding binding;

        public MyViewHolder(@NonNull OrderedProductItemLayoutBinding itemView) {
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

