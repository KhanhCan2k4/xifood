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
import vn.edu.tdc.xifood.databinding.OrderedProductItemLayoutBinding;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.models.Order;
import vn.edu.tdc.xifood.datamodels.Product;

public class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<OrderedProduct> products = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnUpdatePrice updatePrice;

    private boolean isPurchaseAcitivitiy;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public OnUpdatePrice getUpdatePrice() {

        return updatePrice;

    }



    public void setUpdatePrice(OnUpdatePrice updatePrice) {

        this.updatePrice = updatePrice;

    }

    public OrderedProductAdapter(Activity context, ArrayList<OrderedProduct> products, boolean isPurchaseAcitivitiy) {

        this.context = context;

        this.products = products;

        this.isPurchaseAcitivitiy = isPurchaseAcitivitiy;

    }

    public OrderedProductAdapter(Activity context, ArrayList<OrderedProduct> products) {
        this.context = context;
        this.products = products;
        this.isPurchaseAcitivitiy = false;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(OrderedProductItemLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderedProduct product = products.get(position);

        holder.binding.productName.setText(product.getProduct().getName());
        ImageStorageReference.setImageInto(holder.binding.productImage, product.getProduct().getImage().get(0));

        holder.binding.txtAmount.setText(product.getAmount() + "");

        holder.binding.btnDecreaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPurchaseAcitivitiy =false) {
                    product.setAmount(product.getAmount() - 1);
                    holder.binding.txtAmount.setText(product.getAmount() + "");
                }
            }
        });

        holder.binding.btnIncreaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPurchaseAcitivitiy = false) {
                    product.setAmount(product.getAmount() + 1);
                    holder.binding.txtAmount.setText(product.getAmount() + "");
                }
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
    public interface OnUpdatePrice{

        public long updatePrice(long price, int total);

    }
}

