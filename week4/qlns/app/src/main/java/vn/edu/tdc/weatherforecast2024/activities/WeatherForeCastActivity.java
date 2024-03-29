package vn.edu.tdc.weatherforecast2024.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.tdc.weatherforecast2024.adapters.MyRecycleViewAdapter;
import vn.edu.tdc.weatherforecast2024.adapters.MyWeatherAdapter;
import vn.edu.tdc.weatherforecast2024.data.MyData;
import vn.edu.tdc.weatherforecast2024.data.MyWeatherData;
import vn.edu.tdc.weatherforecast2024.databinding.WeatherForecastLayoutBinding;
import vn.edu.tdc.weatherforecast2024.models.City;
import vn.edu.tdc.weatherforecast2024.models.MyCity;
import vn.edu.tdc.weatherforecast2024.models.MyWeather;
import vn.edu.tdc.weatherforecast2024.models.MyWeatherList;
import vn.edu.tdc.weatherforecast2024.models.WeatherAPI;

public class WeatherForeCastActivity extends AppCompatActivity {

    private WeatherForecastLayoutBinding binding;
    private ArrayList<MyCity> cities;
    private ArrayList<MyWeather> weathers;
    private MyWeatherAdapter myWeatherAdapter;
    private WeatherAPI weatherAPI;
    public String key = "940a39e5fe69c802f9b6765b11a46304";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = WeatherForecastLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get data
        cities = MyWeatherData.getCities();

        ArrayList<String> cityNames = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            cityNames.add(cities.get(i).toString());
        }

        //set Data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityNames);
        binding.spLocation.setAdapter(adapter);

        weathers = cities.get(0).getWeathers();
        myWeatherAdapter = new MyWeatherAdapter(this, weathers);
        LinearLayoutManager manager = new LinearLayoutManager(WeatherForeCastActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.listForecast.setLayoutManager(manager);
        binding.listForecast.setAdapter(myWeatherAdapter);

        //set weather
        setWeather(cities.get(0));

        binding.spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                setWeather(cities.get(i));
                getWeathers(adapterView.getSelectedItem().toString(), weathers);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setWeather(MyCity city) {
        weathers = city.getWeathers();
        myWeatherAdapter.setList(weathers);
        myWeatherAdapter.notifyDataSetChanged();
    }

    private void getWeathers(String city, ArrayList<MyWeather> weathers) {
        weathers.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherAPI.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherAPI = retrofit.create(WeatherAPI.class);

        Call<MyWeatherList> call = weatherAPI.getWeathers(city, key);

        call.enqueue(new Callback<MyWeatherList>() {
            @Override
            public void onResponse(Call<MyWeatherList> call, Response<MyWeatherList> response) {
                if(response.isSuccessful()) {
                    MyWeatherList weatherList = response.body();
                    assert weatherList != null;

                    weathers.addAll(weatherList.getWeathers());
                    myWeatherAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MyWeatherList> call, Throwable t) {

            }
        });


    }
}