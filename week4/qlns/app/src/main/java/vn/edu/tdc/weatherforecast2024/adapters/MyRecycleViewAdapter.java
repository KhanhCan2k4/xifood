package vn.edu.tdc.weatherforecast2024.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.weatherforecast2024.R;
import vn.edu.tdc.weatherforecast2024.models.City;
import vn.edu.tdc.weatherforecast2024.models.Weather;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<Weather> list;
    private ItemClickListener itemClickListener;

    public void setList(ArrayList<Weather> list) {
        this.list = list;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MyRecycleViewAdapter(Activity context, ArrayList<Weather> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(vn.edu.tdc.weatherforecast2024.databinding.ListItemBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //get data at position
        Weather weather = list.get(position);

        switch (weather.getWeatherType()) {
            case 1:
                holder.binding.imgLoaiThoiTiet.setImageResource(R.drawable.clear);
                break;
            case 2:
                holder.binding.imgLoaiThoiTiet.setImageResource(R.drawable.clounds);
                break;
            case 3:
                holder.binding.imgLoaiThoiTiet.setImageResource(R.drawable.rain);
                break;
            default:
                holder.binding.imgLoaiThoiTiet.setImageResource(R.drawable.unknown);
                break;
        }

        String txtNhietDo = String.format("Max: %d Now: %d Min: %d", weather.getMaxTem(), weather.getNowTem(), weather.getMinTem());
        holder.binding.txtNhietDo.setText(txtNhietDo);
        holder.binding.txtTocDoGio.setText(weather.getWindSpeed() + " Km/h");
        holder.binding.txtTamNhin.setText(weather.getSight() + " m");
        holder.binding.txtThoiGian.setText(weather.getDateTimeText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //define ViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private vn.edu.tdc.weatherforecast2024.databinding.ListItemBinding binding;

        public MyViewHolder(@NonNull vn.edu.tdc.weatherforecast2024.databinding.ListItemBinding itemView) {
            super(itemView.getRoot());

            binding = (vn.edu.tdc.weatherforecast2024.databinding.ListItemBinding) itemView;

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
