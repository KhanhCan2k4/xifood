package vn.edu.tdc.xifood.adapters;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import vn.edu.tdc.xfood.databinding.ToppingItemtBinding;
import vn.edu.tdc.xifood.datamodels.Topping;

public class ToppinAdapter extends RecyclerView.Adapter<ToppinAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<Topping> toppings;
    private ItemClick itemClick;
    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public ArrayList<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(ArrayList<Topping> toppings) {
        this.toppings = toppings;
    }

    public ItemClick getItemClick() {
        return itemClick;
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public ToppinAdapter(Activity context, ArrayList<Topping> toppings) {
        this.context = context;
        this.toppings = toppings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ToppingItemtBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topping topping = toppings.get(position);
        holder.toppingItemtBinding.toppingName.setText(topping.getName());
        holder.toppingItemtBinding.toppingPrice.setText(topping.getPrice() + " ");
        holder.toppingItemtBinding.totalTopping.setText(0 + "");
        holder.setPosition(position);

        if (itemClick != null) {
            holder.toppingItemtBinding.addTopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.onIncreaseAmount(holder);
                }
            });

            holder.toppingItemtBinding.minusTopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.onDecreaseAmount(holder);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return toppings.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ToppingItemtBinding toppingItemtBinding;

        public ToppingItemtBinding getToppingItemtBinding() {
            return toppingItemtBinding;
        }

        public void setToppingItemtBinding(ToppingItemtBinding toppingItemtBinding) {
            this.toppingItemtBinding = toppingItemtBinding;
        }

        private int position;

        public int getThisPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public ViewHolder(@NonNull ToppingItemtBinding itemView) {
            super(itemView.getRoot());
            toppingItemtBinding = itemView;
        }
    }

    public interface ItemClick {
        public void onIncreaseAmount(ViewHolder holder);
        public void onDecreaseAmount(ViewHolder holder);
    }
}
