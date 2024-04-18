//package com.example.doan.Adapter;
//
//import android.app.Activity;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.doan.CartAcitvity;
//import com.example.doan.Model.Cart;
//import com.example.doan.databinding.CartItemtBinding;
//
//
//import java.util.ArrayList;
//
//public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
//    private Activity context;
//    private ArrayList<Cart> carts = new ArrayList<>();
//    private AdapterView.OnItemClickListener clickListener;
//
//    public Activity getContext() {
//        return context;
//    }
//
//    public void setContext(Activity context) {
//        this.context = context;
//    }
//    public ArrayList<Cart> getCarts() {
//        return carts;
//    }
//
//    public void setCarts(ArrayList<Cart> carts) {
//        this.carts = carts;
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
//    public CartAdapter(Activity context, ArrayList<Cart> carts, AdapterView.OnItemClickListener clickListener) {
//        this.context = context;
//        this.carts = carts;
//        this.clickListener = clickListener;
//    }
//
//    public CartAdapter(CartAcitvity cartAcitvity, ArrayList<Cart> carts) {
//    }
//
//    @NonNull
//    @Override
//    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new CartAdapter.ViewHolder(CartItemtBinding.inflate(context.getLayoutInflater(), parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
//Cart c = carts.get(position);
//        Log.d("klo", "onBindViewHolder: "+c);
//holder.cartItemtBinding.giaSanPham.setText(c.getGia()+"");
//holder.cartItemtBinding.hinhSanPham.setImageResource(c.getHinhAnh());
//holder.cartItemtBinding.noteSanPham.setText(c.getGhiChu());
//holder.cartItemtBinding.sttSanPham.setText(c.getThuTuSanPham());
//holder.cartItemtBinding.soLuongSanPham.setText(c.getSoLuong()+"");
//holder.cartItemtBinding.giaSanPham.setText(c.getGia()+"");
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return carts.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private CartItemtBinding cartItemtBinding;
//        public ViewHolder(@NonNull CartItemtBinding itemView) {
//            super(itemView.getRoot());
//            cartItemtBinding = itemView;
//            cartItemtBinding.getRoot().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//
//        }
//    }
//}
