package vn.edu.tdc.weatherforecast2024.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class City {
    private String name;
    private ArrayList<Weather> weathers;

    public ArrayList<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(Weather... weathers) {
        for (int i = 0; i < weathers.length; i++) {
            this.weathers.add(weathers[i]);
        }
    }

    public City(String name) {
        this.name = name;
        this.weathers = new ArrayList<>();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
