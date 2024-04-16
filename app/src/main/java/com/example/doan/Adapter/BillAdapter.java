package com.example.doan.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.Bill;
import com.example.doan.databinding.BillItemtBinding;

import java.util.ArrayList;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private Activity contenxt;
    private ArrayList<Bill> bills;
    private AdapterView.OnItemClickListener clickListener;

    public Activity getContenxt() {
        return contenxt;
    }

    public void setContenxt(Activity contenxt) {
        this.contenxt = contenxt;
    }

    public ArrayList<Bill> getBills() {
        return bills;
    }

    public void setBills(ArrayList<Bill> bills) {
        this.bills = bills;
    }

    public AdapterView.OnItemClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(AdapterView.OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public BillAdapter(Activity contenxt, ArrayList<Bill> bills) {
        this.contenxt = contenxt;
        this.bills = bills;
        this.clickListener = clickListener;
    }

    public BillAdapter() {
    }

    @NonNull
    @Override
    public BillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillAdapter.ViewHolder(BillItemtBinding.inflate(contenxt.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.ViewHolder holder, int position) {
        Bill bill = bills.get(position);
        holder.biding.itemBillName.setText(bill.getName());
        holder.biding.totalItemBill.setText(bill.getSoLuong() + "");
        holder.biding.priceItemtBill.setText(bill.getGia() + "");
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private BillItemtBinding biding;

        public ViewHolder(@NonNull BillItemtBinding itemView) {
            super(itemView.getRoot());
            biding = itemView;
            biding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
