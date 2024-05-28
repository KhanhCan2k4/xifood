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
import vn.edu.tdc.xifood.datamodels.Topping;

public class ToppinAdapter extends RecyclerView.Adapter<ToppinAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Topping> toppings;
    private Map<Topping, Integer> toppingsWithAmount;

    public interface OnToppingCheckedChangeListener {
        void onCheckedChanged(Topping topping, boolean isChecked);
    }

    private OnToppingCheckedChangeListener listener;

    public ToppinAdapter(Context context, ArrayList<Topping> toppings, Map<Topping, Integer> toppingsWithAmount, OnToppingCheckedChangeListener listener) {
        this.context = context;
        this.toppings = toppings;
        this.toppingsWithAmount = toppingsWithAmount;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.topping_itemt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topping topping = toppings.get(position);
        holder.nameTopping.setText(topping.getName());
        holder.priceTopping.setText(topping.getPrice()+"");
        holder.checkbox.setOnCheckedChangeListener(null); // Disable listener to prevent triggering previous listener

        // Set checked status based on toppingsWithAmount map
        holder.checkbox.setChecked(toppingsWithAmount.containsKey(topping) && toppingsWithAmount.get(topping) > 0);

        // Set listener for checkbox
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Notify listener about checkbox state change
                listener.onCheckedChanged(topping, isChecked);
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
}
