package vn.edu.tdc.weatherforecast2024.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyWeather {
    @SerializedName("main")
    Main main;
    @SerializedName("wind")
    Wind windSpeed;
    @SerializedName("visibility")
    int sight;
    @SerializedName("weather")
    ArrayList<WeatherItem> weatherItems = new ArrayList<>();
    @SerializedName("dt_txt")
    String dateTimeText;

    public MyWeather(Main main, Wind windSpeed, int sight, ArrayList<WeatherItem> weatherItems, String dateTimeText) {
        this.main = main;
        this.windSpeed = windSpeed;
        this.sight = sight;
        this.weatherItems = weatherItems;
        this.dateTimeText = dateTimeText;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Wind windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getSight() {
        return sight;
    }

    public void setSight(int sight) {
        this.sight = sight;
    }

    public ArrayList<WeatherItem> getWeatherItems() {
        return weatherItems;
    }

    public void setWeatherItems(ArrayList<WeatherItem> weatherItems) {
        this.weatherItems = weatherItems;
    }

    public String getDateTimeText() {
        return dateTimeText;
    }

    public void setDateTimeText(String dateTimeText) {
        this.dateTimeText = dateTimeText;
    }

    //
    public class Main {
        @SerializedName("temp")
        float maxTem;
        @SerializedName("temp_max")
        float nowTem;
        @SerializedName("temp_min")
        float minTem;

        public Main(float maxTem, float nowTem, float minTem) {
            this.maxTem = maxTem;
            this.nowTem = nowTem;
            this.minTem = minTem;
        }

        public float getMaxTem() {
            return maxTem - 273.15f;
        }

        public void setMaxTem(float maxTem) {
            this.maxTem = maxTem;
        }

        public float getNowTem() {
            return  - 273.15f;
        }

        public void setNowTem(float nowTem) {
            this.nowTem = nowTem;
        }

        public float getMinTem() {
            return minTem - 273.15f;
        }

        public void setMinTem(float minTem) {
            this.minTem = minTem;
        }
    }

    public class WeatherItem {
        @SerializedName("main")
        String weather;

        public WeatherItem(String weather) {
            this.weather = weather;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }
    }

    public class Wind {
        @SerializedName("speed")
        float speed;

        public Wind(float speed) {
            this.speed = speed;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }
    }
}
