package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.tdc.xifood.activities.DetailActivity;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.databinding.StaffOrderItemLayoutBinding;
import vn.edu.tdc.xifood.datamodels.Order;
import vn.edu.tdc.xifood.datamodels.OrderedProduct;
import vn.edu.tdc.xifood.datamodels.Product;

public class StaffOrderAdapter extends RecyclerView.Adapter<StaffOrderAdapter.ViewHolder> {
    private Order order;
    private Activity context;

    private ItemDelete itemInteract;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ItemDelete getItemInteract() {
        return itemInteract;
    }

    public void setItemInteract(ItemDelete itemInteract) {
        this.itemInteract = itemInteract;
    }

    public StaffOrderAdapter(Activity context, Order order) {
        this.order = order;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(StaffOrderItemLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StaffOrderAdapter.ViewHolder holder, int position) {
        OrderedProduct orderedProduct = order.getOrderedProducts().get(position);

        holder.binding.productName.setText(orderedProduct.getProduct().getName());
        ImageStorageReference.setImageInto(holder.binding.productImage, orderedProduct.getProduct().getImage().get(0));

        holder.binding.txtAmount.setText(orderedProduct.getAmount() +"");
        holder.binding.btnIncreaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderedProduct.getAmount() < DetailActivity.MAX_AMOUNT) {
                    orderedProduct.setAmount(orderedProduct.getAmount() + 1);
                    holder.binding.txtAmount.setText(orderedProduct.getAmount() +"");
                    if (itemInteract != null) {
                        itemInteract.onUpdateTotal();
                    }
                }
            }
        });
        holder.binding.btnDecreaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderedProduct.getAmount() > 1) {
                    orderedProduct.setAmount(orderedProduct.getAmount() - 1);
                    holder.binding.txtAmount.setText(orderedProduct.getAmount() +"");
                    if (itemInteract != null) {
                        itemInteract.onUpdateTotal();
                    }
                }
            }
        });

        String toppings = "";
        for (String topping: orderedProduct.getToppings().keySet()) {
            long price = orderedProduct.getToppings().get(topping);
            toppings += String.format("- %s \t\t\t\t\t %s\n", topping, Product.getPriceInFormat(price));
        }

        holder.binding.productPrice.setText(Product.getPriceInFormat(orderedProduct.getProduct().getPrice()));

        holder.binding.toppings.setText(toppings);

        holder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemInteract != null) {
                    itemInteract.onDelete(holder, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return order.getOrderedProducts().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private StaffOrderItemLayoutBinding binding;

        public ViewHolder(@NonNull StaffOrderItemLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface ItemDelete {
        void onDelete(ViewHolder viewHolder, int position);
        void onUpdateTotal();
    }
}
