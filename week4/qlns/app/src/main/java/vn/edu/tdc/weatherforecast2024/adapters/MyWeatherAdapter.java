package vn.edu.tdc.weatherforecast2024.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.weatherforecast2024.R;
import vn.edu.tdc.weatherforecast2024.models.MyWeather;
import vn.edu.tdc.weatherforecast2024.models.Weather;

public class MyWeatherAdapter extends RecyclerView.Adapter<MyWeatherAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<MyWeather> list;
    private ItemClickListener itemClickListener;

    public void setList(ArrayList<MyWeather> list) {
        this.list = list;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MyWeatherAdapter(Activity context, ArrayList<MyWeather> list) {
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
        MyWeather weather = list.get(position);

        if(weather.getWeatherItems().get(0).getWeather().equalsIgnoreCase("Clear")) {
            holder.binding.imgLoaiThoiTiet.setImageResource(R.drawable.clear);
        } else if(weather.getWeatherItems().get(0).getWeather().equalsIgnoreCase("Clouds")) {
            holder.binding.imgLoaiThoiTiet.setImageResource(R.drawable.clounds);
        } else if(weather.getWeatherItems().get(0).getWeather().equalsIgnoreCase("Rain")) {
            holder.binding.imgLoaiThoiTiet.setImageResource(R.drawable.rain);
        } else {
            holder.binding.imgLoaiThoiTiet.setImageResource(R.drawable.unknown);
        }

        String txtNhietDo = String.format("Max: %.2f Now: %.2f Min: %.2f", weather.getMain().getMaxTem(), weather.getMain().getNowTem(), weather.getMain().getMinTem());
        holder.binding.txtNhietDo.setText(txtNhietDo);
        holder.binding.txtTocDoGio.setText(weather.getWindSpeed().getSpeed() + " Km/h");
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
