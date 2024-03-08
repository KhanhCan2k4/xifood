package vn.edu.tdc.quanlynhansu2023.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import vn.edu.tdc.quanlynhansu2023.R;
import vn.edu.tdc.quanlynhansu2023.databinding.ListItemBinding;
import vn.edu.tdc.quanlynhansu2023.databinding.ListItemInvertBinding;
import vn.edu.tdc.quanlynhansu2023.models.MyPerson;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<MyPerson> list;

    private ItemClickListener itemClickListener;

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MyRecycleViewAdapter(Activity context, ArrayList<MyPerson> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ListItemBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //get data at position
        MyPerson person = list.get(position);

        //render new data into RecycleView's item
        holder.binding.lblName.setText(person.getName());

        if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radCaoDang))) {
            holder.binding.imgDegree.setImageResource(R.mipmap.college);
        }
        else if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radDaiHoc))) {
            holder.binding.imgDegree.setImageResource(R.mipmap.university);
        }
        else if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radTrungCap))) {
            holder.binding.imgDegree.setImageResource(R.mipmap.midium);
        }
        else {
            holder.binding.imgDegree.setImageResource(R.mipmap.none);
        }

        holder.binding.lblHobbies.setText(person.getHobbies());

        //update new position for RecycleView's Item
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //define ViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ListItemBinding binding;
        private int position;

        public void setPosition(int position) {
            this.position = position;
        }
        public int getRecycleViewPosition() {
            return position;
        }

        public ListItemBinding getBinding() {
            return binding;
        }

        public void setBinding(ListItemBinding binding) {
            this.binding = binding;
        }


        public MyViewHolder(@NonNull ListItemBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener != null) {
                        itemClickListener.onItemClick(MyViewHolder.this);
                    }
                    else {
                        Log.d("TAG-adapter", "You must create an ItemClickListener");
                    }
                }
            });

        }

    }

    //define Interface to authorize User' side
    public interface ItemClickListener{
        public void onItemClick(MyViewHolder viewHolder);
    }
}
