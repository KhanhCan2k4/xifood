package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.ImageStorageReference;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.OrderedProductItemLayoutBinding;
import vn.edu.tdc.xifood.databinding.OrderedProductPurchaseLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.Order.OrderedProduct;
import vn.edu.tdc.xifood.mydatamodels.Product;
import vn.edu.tdc.xifood.mydatamodels.Topping;

public class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<OrderedProduct> products = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnUpdatePrice updatePrice;
    private boolean isPurchaseAcitivitiy;
    private boolean isViewMode;
    private boolean isStaffMode;
    private OnTakeNoteListener takeNoteListener;

    public OnTakeNoteListener getTakeNoteListener() {
        return takeNoteListener;
    }

    public void setTakeNoteListener(OnTakeNoteListener takeNoteListener) {
        this.takeNoteListener = takeNoteListener;
    }

    public void setViewMode(boolean viewMode) {
        isViewMode = viewMode;
    }

    public void setStaffMode(boolean staffMode) {
        isStaffMode = staffMode;
    }

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
        this.isViewMode = false;
        this.isStaffMode = false;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isPurchaseAcitivitiy) {
            return new MyViewHolder(OrderedProductPurchaseLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
        }
        return new MyViewHolder(OrderedProductItemLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderedProduct product = products.get(position);
        SharePreference.setSharedPreferences(context);

        API<Product> productAPI = new API<>(Product.class, API.PRODUCT_TABLE_NAME);
        productAPI.find(product.getKey(), new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                Product p = (Product) object;

                if (isPurchaseAcitivitiy) {
                    holder.bindingPurchase.productName.setText(p.getName());
                    holder.bindingPurchase.productPrice.setText(Product.getPriceInFormat(p.getPrice()));
                    holder.bindingPurchase.productDes.setText(p.getDescription());
                    if (p.getImage().size() > 0) {
                        ImageStorageReference.setImageInto(holder.bindingPurchase.productImg, p.getImage().get(0));
                    }
                } else {
                    holder.binding.productName.setText(p.getName());
                    if (p.getImage().size() > 0) {
                        ImageStorageReference.setImageInto(holder.binding.productImage, p.getImage().get(0));
                    }
                }
            }
        });

        if (isPurchaseAcitivitiy) {
            holder.bindingPurchase.amount.setSelection(product.getQuantity() - 1);

            API<Topping> toppingAPI = new API<>(Topping.class, API.TOPPING_TABLE_NAME);
            ArrayList<Topping> toppings = new ArrayList<>();
            ToppinAdapter toppinAdapter = new ToppinAdapter(context, toppings);
            toppinAdapter.setCheckedAll(true);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            holder.bindingPurchase.toppingList.setLayoutManager(manager);
            holder.bindingPurchase.toppingList.setAdapter(toppinAdapter);

            holder.bindingPurchase.orderNote.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!hasObservers() && takeNoteListener != null) {
                        takeNoteListener.onTakeNote(holder.bindingPurchase.orderNote.getText().toString(), holder.getAdapterPosition());
                    }
                }
            });

//            holder.bindingPurchase.orderNote.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (takeNoteListener != null) {
//                        takeNoteListener.onTakeNote(charSequence.toString(), holder.getAdapterPosition());
//                    }
//                    holder.bindingPurchase.orderNote.setFocusable(true);
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });

            if (!isViewMode || !isStaffMode) {
                holder.bindingPurchase.reviewFrame.setVisibility(View.GONE);
            } else {
                holder.bindingPurchase.reviewFrame.setVisibility(View.VISIBLE);

                holder.bindingPurchase.amount.setSelection(product.getQuantity() -1);
                holder.bindingPurchase.amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        holder.bindingPurchase.amount.setSelection(product.getQuantity() -1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                holder.bindingPurchase.orderNote.setText(product.getNote());
                holder.bindingPurchase.orderNote.setEnabled(false);

                holder.bindingPurchase.review.setSelection(product.getRating() - 1 > 0 ? product.getRating() - 1 : 4 );
                holder.bindingPurchase.txtReview.setText(product.getReview());

                if (!product.getReview().isEmpty()) {
                    holder.bindingPurchase.sendReview.setVisibility(View.GONE);
                }

                holder.bindingPurchase.sendReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (takeNoteListener != null) {
                            String review = holder.bindingPurchase.txtReview.getText().toString().trim();
                            int rating;
                            try {
                                rating = Integer.parseInt(holder.bindingPurchase.review.getSelectedItem().toString().trim());
                            } catch (Exception e) {
                                rating = 0;
                            }
                            takeNoteListener.sendReview(review, rating, holder.getAdapterPosition());
                        }
                    }
                });
            }

            toppingAPI.all(new API.FirebaseCallbackAll() {
                @Override
                public void onCallback(ArrayList list) {
                    toppings.clear();

                    for (Object o : list) {
                        Topping p = (Topping) o;
                        if (product.getToppings().contains(p.getKey())) {
                            toppings.add(p);
                        }
                    }

                    toppinAdapter.notifyDataSetChanged();
                }
            });

        } else {
            holder.binding.txtAmount.setText(product.getQuantity() + "");
            holder.binding.btnDecreaseAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isPurchaseAcitivitiy = false) {
                        product.setQuantity(product.getQuantity() - 1);
                        holder.binding.txtAmount.setText(product.getQuantity() + "");
                    }
                }
            });
            holder.binding.btnIncreaseAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isPurchaseAcitivitiy = false) {
                        product.setQuantity(product.getQuantity() + 1);
                        holder.binding.txtAmount.setText(product.getQuantity() + "");
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private OrderedProductItemLayoutBinding binding;
        private OrderedProductPurchaseLayoutBinding bindingPurchase;

        public MyViewHolder(@NonNull OrderedProductItemLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public MyViewHolder(@NonNull OrderedProductPurchaseLayoutBinding itemView) {
            super(itemView.getRoot());
            bindingPurchase = itemView;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(MyViewHolder viewHolder);

        public void onIncreaseAmount(MyViewHolder viewHolder);

        public void onDecreaseAmount(MyViewHolder viewHolder);
    }

    public interface OnUpdatePrice {

        public long updatePrice(long price, int total);

    }

    public interface OnTakeNoteListener {
        void onTakeNote(String note,int position);

        void sendReview(String review, int rating, int position);
    }
}

