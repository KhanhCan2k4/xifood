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
import java.util.Map;
import java.util.Set;

import vn.edu.tdc.xifood.apis.ToppingAPI;
import vn.edu.tdc.xifood.databinding.ToppingItemtBinding;
import vn.edu.tdc.xifood.datamodels.Topping;

public class ToppinAdapter extends RecyclerView.Adapter<ToppinAdapter.ViewHolder> {

    private Activity context;
    private Map<String, Integer> toppingAndTotall;
    private ArrayList<Topping> toppings;

    private AdapterView.OnItemClickListener clickListener;
    int soluong = 0;

    public ToppinAdapter(Activity context, ArrayList<Topping> toppings) {
        Log.d("soluongTopping", "ToppinAdapter: " + toppings.size());
        this.context = context;
        this.toppings = toppings;
        this.toppingAndTotall = new Map<String, Integer>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean containsKey(@Nullable Object key) {
                return false;
            }

            @Override
            public boolean containsValue(@Nullable Object value) {
                return false;
            }

            @Nullable
            @Override
            public Integer get(@Nullable Object key) {
                return null;
            }

            @Nullable
            @Override
            public Integer put(String key, Integer value) {
                return null;
            }

            @Nullable
            @Override
            public Integer remove(@Nullable Object key) {
                return null;
            }

            @Override
            public void putAll(@NonNull Map<? extends String, ? extends Integer> m) {

            }

            @Override
            public void clear() {

            }

            @NonNull
            @Override
            public Set<String> keySet() {
                return null;
            }

            @NonNull
            @Override
            public Collection<Integer> values() {
                return null;
            }

            @NonNull
            @Override
            public Set<Entry<String, Integer>> entrySet() {
                return null;
            }
        };
    }

    @NonNull
    @Override
    public ToppinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ToppingItemtBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToppinAdapter.ViewHolder holder, int position) {

        Topping t = new Topping(toppings.get(position));
        holder.toppingItemtBinding.addTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluong++;
                holder.toppingItemtBinding.totalTopping.setText(soluong + "");
            }
        });
        holder.toppingItemtBinding.minusTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soluong > 0) {
                    soluong--;
                    holder.toppingItemtBinding.totalTopping.setText(soluong + "");
                } else {
                    holder.toppingItemtBinding.totalTopping.setText(soluong + "");
                }
            }
        });
        Log.d("TenCuaTopping", "onBindViewHolder: " + t.getName());
        holder.toppingItemtBinding.toppingName.setText(t.getName());
        holder.toppingItemtBinding.toppingPrice.setText(t.getPrice() + "");
        toppingAndTotall.put(t.getKey(), soluong);

//        ToppingAPI.all(new ToppingAPI.FirebaseCallbackAll() {
//            @Override
//            public void onCallback(ArrayList<Topping> t) {
//                for(int i=0; i<t.size();i++)
//                {
//                    toppings.put(t.get(i).getKey(),0);
//
//                    holder.toppingItemtBinding.toppingName.setText(t.get(i).getName());
//                }
//
//            }
//        });
//        ToppingAPI.all(new ToppingAPI.FirebaseCallbackAll() {
//            @Override
//            public void onCallback(ArrayList<Topping> t) {
//                Log.d("Topping", t.size()+"");
//                for(int i=0; i<t.size();i++)
//                {
//                    holder.toppingItemtBinding.addTopping.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            soluong++;
//                            holder.toppingItemtBinding.totalTopping.setText(soluong+"");
//                        }
//                    });
//                    holder.toppingItemtBinding.minusTopping.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if(soluong >0)
//                            {
//                                soluong--;
//                                holder.toppingItemtBinding.totalTopping.setText(soluong+"");
//                            }
//                            else {
//                                holder.toppingItemtBinding.totalTopping.setText(soluong+"");
//                            }
//                        }
//                    });
//
//                    holder.toppingItemtBinding.toppingName.setText(t.get(i).getName());
//                    holder.toppingItemtBinding.toppingPrice.setText(t.get(i).getPrice()+"");
//                    toppings.put(t.get(i).getKey(),soluong);
//
//                }
//            }
//
//        });

    }

    @Override
    public int getItemCount() {

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

//    public Activity getContext() {
//        return context;
//    }
//
//    public void setContext(Activity context) {
//        this.context = context;
//    }
//
//    public Map<String, Integer> getToppings() {
//        return toppings;
//    }
//
//    public AdapterView.OnItemClickListener getClickListener() {
//        return clickListener;
//    }
//
//    public void setClickListener(AdapterView.OnItemClickListener clickListener) {
//        this.clickListener = clickListener;
//    }
//
//
//    public ToppinAdapter(Activity context, ArrayList<Topping> toppings) {
//        this.context = context;
//        this.toppings = toppings;
//        this.clickListener = clickListener;
//    }
//
//
//
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder(ToppingItemtBinding.inflate(context.getLayoutInflater(), parent, false));
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Topping topping =toppings.get(position);
//        holder.toppingItemtBinding.toppingName.setText(topping.getName());
//        holder.toppingItemtBinding.toppingPrice.setText(topping.getPrice()+" ");
//
//        holder.toppingItemtBinding.minusTopping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                topping.setSoluong(topping.getSoluong()-1);
//                if(topping.getSoluong()<0)
//                {
//                    holder.toppingItemtBinding.totalTopping.setText("0");
//                }
//                else {
//                    holder.toppingItemtBinding.totalTopping.setText(topping.getSoluong() + " ");
//                }
//            }
//        });
//        holder.toppingItemtBinding.addTopping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(topping.getSoluong()<0)
//                {
//                    topping.setSoluong(0);
//                }
//                topping.setSoluong(topping.getSoluong()+1);
//
//                holder.toppingItemtBinding.totalTopping.setText(topping.getSoluong() + " ");
//
//            }
//        });
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return toppings.size();
//    }
//
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        private ToppingItemtBinding toppingItemtBinding;
//        public ViewHolder(@NonNull ToppingItemtBinding itemView) {
//            super(itemView.getRoot());
//            toppingItemtBinding = itemView;
//            toppingItemtBinding.getRoot().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//
//        }
//    }
//


}
