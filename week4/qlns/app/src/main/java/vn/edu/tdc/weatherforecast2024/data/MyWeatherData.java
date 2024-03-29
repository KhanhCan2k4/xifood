package vn.edu.tdc.weatherforecast2024.data;

import java.time.LocalDateTime;
import java.util.ArrayList;

import vn.edu.tdc.weatherforecast2024.models.City;
import vn.edu.tdc.weatherforecast2024.models.MyCity;
import vn.edu.tdc.weatherforecast2024.models.Weather;

public class MyWeatherData {
    private static ArrayList<MyCity> cities;

    public final String key = "940a39e5fe69c802f9b6765b11a46304";

    public static ArrayList<MyCity> getCities() {
        if(cities == null) {
            cities = new ArrayList<>();

            MyCity tp1 = new MyCity("Ho Chi Minh");
            MyCity tp2 = new MyCity("Danang");
            MyCity tp3 = new MyCity("Hanoi");
            MyCity tp4 = new MyCity("Haiphong");
            MyCity tp5 = new MyCity("London");
            MyCity tp6 = new MyCity("New York");
            MyCity tp7 = new MyCity("Tokyo");

            cities.add(tp1);
            cities.add(tp2);
            cities.add(tp3);
            cities.add(tp4);
            cities.add(tp5);
            cities.add(tp6);
            cities.add(tp7);
        }

        return cities;
    }
}
