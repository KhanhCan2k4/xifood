package vn.edu.tdc.weatherforecast2024.data;

import java.time.LocalDateTime;
import java.util.ArrayList;

import vn.edu.tdc.weatherforecast2024.models.City;
import vn.edu.tdc.weatherforecast2024.models.Weather;

public class MyData {
    public final static  int CLEAR_WEATHER = 1;
    public final static  int CLOUDS_WEATHER = 2;
    public final static  int RAIN_WEATHER = 3;
    public final static  int UNKNOWN_WEATHER = 4;
    private static ArrayList<City> cities;

    public static ArrayList<City> getCities() {
        if(cities == null) {
            cities = new ArrayList<>();

            Weather w1 = new Weather();
            w1.setMaxTem(23);
            w1.setMinTem(22);
            w1.setNowTem(23);
            w1.setWindSpeed(3.24f);
            w1.setSight(10000);
            w1.setTime(LocalDateTime.of(2021, 2, 8, 15, 0, 0) );
            w1.setDateTimeText(w1.getTime().toString());
            w1.setWeatherType(RAIN_WEATHER);

            Weather w2 = new Weather();
            w2.setMaxTem(22);
            w2.setMinTem(22);
            w2.setNowTem(22);
            w2.setWindSpeed(1.64f);
            w2.setSight(10000);
            w2.setTime(LocalDateTime.of(2021, 2, 8, 18, 0, 0) );
            w2.setDateTimeText(w2.getTime().toString());
            w2.setWeatherType(RAIN_WEATHER);

            Weather w3 = new Weather();
            w3.setMaxTem(22);
            w3.setMinTem(22);
            w3.setNowTem(22);
            w3.setWindSpeed(1.55f);
            w3.setSight(10000);
            w3.setTime(LocalDateTime.of(2021, 2, 8, 21, 0, 0) );
            w3.setDateTimeText(w3.getTime().toString());
            w3.setWeatherType(RAIN_WEATHER);

            Weather w4 = new Weather();
            w4.setMaxTem(22);
            w4.setMinTem(22);
            w4.setNowTem(22);
            w4.setWindSpeed(2.02f);
            w4.setSight(10000);
            w4.setTime(LocalDateTime.of(2021, 2, 8, 23, 0, 0) );
            w4.setDateTimeText(w4.getTime().toString());
            w4.setWeatherType(RAIN_WEATHER);

            Weather w5 = new Weather();
            w5.setMaxTem(24);
            w5.setMinTem(22);
            w5.setNowTem(23);
            w5.setWindSpeed(3.24f);
            w5.setSight(10000);
            w5.setTime(LocalDateTime.of(2021, 2, 8, 15, 0, 0) );
            w5.setDateTimeText(w5.getTime().toString());
            w5.setWeatherType(UNKNOWN_WEATHER);

            Weather w6 = new Weather();
            w6.setMaxTem(22);
            w6.setMinTem(21);
            w6.setNowTem(22);
            w6.setWindSpeed(1.64f);
            w6.setSight(10000);
            w6.setTime(LocalDateTime.of(2021, 2, 8, 18, 0, 0) );
            w6.setDateTimeText(w6.getTime().toString());
            w6.setWeatherType(UNKNOWN_WEATHER);

            Weather w7 = new Weather();
            w7.setMaxTem(22);
            w7.setMinTem(22);
            w7.setNowTem(22);
            w7.setWindSpeed(2.03f);
            w7.setSight(10000);
            w7.setTime(LocalDateTime.of(2021, 2, 8, 21, 0, 0) );
            w7.setDateTimeText(w7.getTime().toString());
            w7.setWeatherType(CLEAR_WEATHER);

            Weather w8 = new Weather();
            w8.setMaxTem(22);
            w8.setMinTem(22);
            w8.setNowTem(22);
            w8.setWindSpeed(2.02f);
            w8.setSight(10000);
            w8.setTime(LocalDateTime.of(2021, 2, 8, 23, 0, 0) );
            w8.setDateTimeText(w8.getTime().toString());
            w8.setWeatherType(CLOUDS_WEATHER);

            Weather w9 = new Weather();
            w9.setMaxTem(24);
            w9.setMinTem(22);
            w9.setNowTem(23);
            w9.setWindSpeed(3.24f);
            w9.setSight(10000);
            w9.setTime(LocalDateTime.of(2021, 2, 8, 15, 0, 0) );
            w9.setDateTimeText(w9.getTime().toString());
            w9.setWeatherType(UNKNOWN_WEATHER);

            Weather w10 = new Weather();
            w10.setMaxTem(22);
            w10.setMinTem(21);
            w10.setNowTem(22);
            w10.setWindSpeed(1.64f);
            w10.setSight(10000);
            w10.setTime(LocalDateTime.of(2021, 2, 8, 18, 0, 0) );
            w10.setDateTimeText(w10.getTime().toString());
            w10.setWeatherType(UNKNOWN_WEATHER);

            Weather w11 = new Weather();
            w11.setMaxTem(22);
            w11.setMinTem(22);
            w11.setNowTem(22);
            w11.setWindSpeed(2.03f);
            w11.setSight(10000);
            w11.setTime(LocalDateTime.of(2021, 2, 8, 21, 0, 0) );
            w11.setDateTimeText(w11.getTime().toString());
            w11.setWeatherType(CLEAR_WEATHER);

            City tp1 = new City("Ho Chi Minh");
            tp1.setWeathers(w1);
            tp1.setWeathers(w2);
            tp1.setWeathers(w3);
            tp1.setWeathers(w4);

            City tp2 = new City("Danang");
            tp2.setWeathers(w5);
            tp2.setWeathers(w6);
            tp2.setWeathers(w7);

            City tp3 = new City("Hanoi");
            tp3.setWeathers(w8);
            tp3.setWeathers(w9);
            tp3.setWeathers(w10);
            tp3.setWeathers(w11);

            cities.add(tp1);
            cities.add(tp2);
            cities.add(tp3);

            City tp4 = new City("Haiphong");
            City tp5 = new City("London");
            City tp6 = new City("New York");
            City tp7 = new City("Tokyo");
            cities.add(tp4);
            cities.add(tp5);
            cities.add(tp6);
            cities.add(tp7);
        }

        return cities;
    }
}
