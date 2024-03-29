package vn.edu.tdc.weatherforecast2024.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    static String baseURL = "https://api.openweathermap.org/";

    @GET("data/2.5/forecast?")
    Call<MyWeatherList> getWeathers(@Query("q") String cityName, @Query("appid") String key);

}
