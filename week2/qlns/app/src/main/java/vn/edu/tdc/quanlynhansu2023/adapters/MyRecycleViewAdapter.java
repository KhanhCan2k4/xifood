package vn.edu.tdc.quanlynhansu2023.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        Log.d("TAG-viewType", "onCreateViewHolder: " + viewType);
        if(viewType == 0) {
            return new MyViewHolder(ListItemBinding.inflate(context.getLayoutInflater(), parent, false));
        } else {
            return new MyViewHolder(ListItemInvertBinding.inflate(context.getLayoutInflater(), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //get data at position
        MyPerson person = list.get(position);

        //update new position for RecycleView's Item
        holder.setPosition(position);

        if(position % 2 == 0) {
            //render new data into RecycleView's item
            holder.binding.lblName.setText(person.getName());

            holder.itemLayout = holder.binding.itemLayout;

            if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radCaoDang))) {
                holder.binding.imgDegree.setImageResource(R.mipmap.college);
            } else if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radDaiHoc))) {
                holder.binding.imgDegree.setImageResource(R.mipmap.university);
            } else if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radTrungCap))) {
                holder.binding.imgDegree.setImageResource(R.mipmap.midium);
            } else {
                holder.binding.imgDegree.setImageResource(R.mipmap.none);
            }

            holder.binding.lblHobbies.setText(person.getHobbies());
        } else {
            //render new data into RecycleView's item
            holder.invertBinding.lblName.setText(person.getName());

            holder.itemLayout = holder.invertBinding.itemLayout;

            if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radCaoDang))) {
                holder.invertBinding.imgDegree.setImageResource(R.mipmap.college);
            } else if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radDaiHoc))) {
                holder.invertBinding.imgDegree.setImageResource(R.mipmap.university);
            } else if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radTrungCap))) {
                holder.invertBinding.imgDegree.setImageResource(R.mipmap.midium);
            } else {
                holder.invertBinding.imgDegree.setImageResource(R.mipmap.none);
            }

            holder.invertBinding.lblHobbies.setText(person.getHobbies());
        }


    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //define ViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ListItemBinding binding;
        private ListItemInvertBinding invertBinding;
        private int position = 0;
        private ConstraintLayout itemLayout;

        public ConstraintLayout getItemLayout() {
            return itemLayout;
        }

        public void setItemLayout(ConstraintLayout itemLayout) {
            this.itemLayout = itemLayout;
        }

        public void setPosition(int position) {
            this.position = position;
        }
        public int getRecycleViewPosition() {
            return position;
        }

        public MyViewHolder(@NonNull ListItemInvertBinding itemView) {
            super(itemView.getRoot());

            invertBinding = (ListItemInvertBinding) itemView;

            invertBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener != null) {
                        itemClickListener.onItemClick(MyViewHolder.this);
                    } else {
                        Log.d("TAG-item-click", "onClick: not set item listener yet");
                    }
                }
            });
        }

        public MyViewHolder(@NonNull ListItemBinding itemView) {
            super(itemView.getRoot());

            binding = (ListItemBinding) itemView;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener != null) {
                        itemClickListener.onItemClick(MyViewHolder.this);
                    } else {
                        Log.d("TAG-item-click", "onClick: not set item listener yet");
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
