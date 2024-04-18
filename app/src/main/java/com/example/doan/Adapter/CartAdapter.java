package com.example.doan.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.CartAcitvity;
import com.example.doan.Model.Cart;
import com.example.doan.databinding.CartItemtBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Activity content;
    private ArrayList<Cart> carts;

    public Activity getContent() {
        return content;
    }

    public void setContent(Activity content) {
        this.content = content;
    }

    public ArrayList<Cart> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Cart> carts) {
        this.carts = carts;
    }

    public CartAdapter(Activity content, ArrayList<Cart> carts) {
        this.content = content;
        this.carts = carts;
    }
    public CartAdapter() {

    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartAdapter.ViewHolder(CartItemtBinding.inflate(content.getLayoutInflater(),parent,false));
        //        return new CartAdapter.ViewHolder(CartItemtBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Cart c = carts.get(position);
        holder.cartBinding.tenSanPham.setText(c.getName());
        holder.cartBinding.giaSanPham.setText(c.getGia()+"");
//        holder.cartBinding.toppingThem.setAdapter(c.getN);
        holder.cartBinding.hinhSanPham.setImageResource(c.getHinhAnh());
        holder.cartBinding.noteSanPham.setText(c.getGhiChu());
        holder.cartBinding.soLuongSanPham.setText(c.getSoLuong()+"");
        holder.cartBinding.sttSanPham.setText(c.getThuTuSanPham()+"");

    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CartItemtBinding cartBinding;

        public ViewHolder(@NonNull CartItemtBinding itemView) {
            super(itemView.getRoot());
            cartBinding = itemView;
        }
    }
}
