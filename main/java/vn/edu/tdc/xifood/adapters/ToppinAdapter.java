package vn.edu.tdc.xifood.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.mydatamodels.Product;
import vn.edu.tdc.xifood.mydatamodels.Topping;

public class ToppinAdapter extends RecyclerView.Adapter<ToppinAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Topping> toppings;
    private OnItemCheckedListener onItemCheckedListener;
    private boolean isCheckedAll;
    private ArrayList<String> comparingList;

    public ArrayList<String> getComparingList() {
        return comparingList;
    }

    public void setComparingList(ArrayList<String> comparingList) {
        this.comparingList = comparingList;
    }

    public void setCheckedAll(boolean checkedAll) {
        isCheckedAll = checkedAll;
    }

    public OnItemCheckedListener getOnItemCheckedListener() {
        return onItemCheckedListener;
    }

    public void setOnItemCheckedListener(OnItemCheckedListener onItemCheckedListener) {
        this.onItemCheckedListener = onItemCheckedListener;
    }

    public ArrayList<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(ArrayList<Topping> toppings) {
        this.toppings = toppings;
    }

    public ToppinAdapter(Context context, ArrayList<Topping> toppings) {
        this.context = context;
        this.toppings = toppings;
        this.isCheckedAll = false;
        this.comparingList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.topping_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topping topping = toppings.get(position);
        holder.nameTopping.setText(topping.getName());
        holder.priceTopping.setText(Product.getPriceInFormat(topping.getPrice()));
        holder.checkbox.setChecked(isCheckedAll || comparingList.contains(topping.getKey()));
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (onItemCheckedListener != null) {
                    onItemCheckedListener.setItemChecked(b, topping.getKey());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return toppings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView nameTopping;
        TextView priceTopping;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.toggle);
            nameTopping = itemView.findViewById(R.id.toppingName);
            priceTopping = itemView.findViewById(R.id.toppingAmount);
        }
    }

    public static interface OnItemCheckedListener {
        void setItemChecked(Boolean isChecked, String key);
    }
}
