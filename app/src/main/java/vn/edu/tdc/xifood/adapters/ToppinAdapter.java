package vn.edu.tdc.xifood.adapters;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import vn.edu.tdc.xifood.apis.ToppingAPI;
import vn.edu.tdc.xifood.databinding.ToppingItemtBinding;
import vn.edu.tdc.xifood.models.Topping;

public class ToppinAdapter extends RecyclerView.Adapter<ToppinAdapter.ViewHolder> {
private Activity context;
private ArrayList<Topping> toppings;
 private AdapterView.OnItemClickListener clickListener;

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

    public AdapterView.OnItemClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(AdapterView.OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public ToppinAdapter(Activity context, ArrayList<Topping> toppings) {
        Log.d("soluongTopping", "ToppinAdapter: " + toppings.size());
        this.context = context;
        this.toppings = toppings;
        this.clickListener = clickListener;
    }




    @NonNull
    @Override
    public ToppinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ToppingItemtBinding.inflate(context.getLayoutInflater(), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     Topping topping =toppings.get(position);
    holder.toppingItemtBinding.toppingName.setText(topping.getName());
    holder.toppingItemtBinding.toppingPrice.setText(topping.getPrice()+" ");
    holder.toppingItemtBinding.totalTopping.setText(topping.getSoluong()+" ");
    }

    @Override
    public int getItemCount() {
      return toppings.size();
            }

        if (toppings != null) {
            return toppings.size();
            // Thực hiện các hoạt động khác trên ArrayList
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       private ToppingItemtBinding toppingItemtBinding;
        public ViewHolder(@NonNull ToppingItemtBinding itemView) {
            super(itemView.getRoot());
            toppingItemtBinding = itemView;
            toppingItemtBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }


}
