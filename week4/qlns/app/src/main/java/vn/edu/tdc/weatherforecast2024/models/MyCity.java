package vn.edu.tdc.weatherforecast2024.models;

import java.util.ArrayList;

public class MyCity {
    private String name;
    private ArrayList<MyWeather> weathers;

    public ArrayList<MyWeather> getWeathers() {
        return weathers;
    }

    public void setWeathers(MyWeather... weathers) {
        for (int i = 0; i < weathers.length; i++) {
            this.weathers.add(weathers[i]);
        }
    }

    public MyCity(String name) {
        this.name = name;
        this.weathers = new ArrayList<>();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
