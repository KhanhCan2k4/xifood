package vn.edu.tdc.weatherforecast2024.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyWeatherList {
    @SerializedName("cod")
    int cod;

    @SerializedName("list")
    ArrayList<MyWeather> weathers = new ArrayList<>();

    public MyWeatherList(int cod, ArrayList<MyWeather> weathers) {
        this.cod = cod;
        this.weathers = weathers;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public ArrayList<MyWeather> getWeathers() {
        return weathers;
    }

    public void setWeathers(ArrayList<MyWeather> weathers) {
        this.weathers = weathers;
    }
}
