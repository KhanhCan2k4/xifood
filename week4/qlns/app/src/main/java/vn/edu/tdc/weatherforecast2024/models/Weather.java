package vn.edu.tdc.weatherforecast2024.models;

import java.time.LocalDateTime;


public class Weather {
    private int maxTem;
    private int nowTem;
    private int minTem;
    private float windSpeed;

    private int sight;
    private LocalDateTime time;
    private int weatherType;
    private String dateTimeText;

    public String getDateTimeText() {
        return dateTimeText;
    }

    public void setDateTimeText(String dateTimeText) {
        this.dateTimeText = dateTimeText;
    }

    public int getMaxTem() {
        return maxTem;
    }

    public void setMaxTem(int maxTem) {
        this.maxTem = maxTem;
    }

    public int getNowTem() {
        return nowTem;
    }

    public void setNowTem(int nowTem) {
        this.nowTem = nowTem;
    }

    public int getMinTem() {
        return minTem;
    }

    public void setMinTem(int minTem) {
        this.minTem = minTem;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getSight() {
        return sight;
    }

    public void setSight(int sight) {
        this.sight = sight;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(int weatherType) {
        this.weatherType = weatherType;
    }

    public Weather() {
        maxTem = 0;
        nowTem = 0;
        minTem = 0;
        windSpeed = 0.0f;
        sight = 0;
        time = LocalDateTime.now();
        weatherType = 0;
        dateTimeText = LocalDateTime.now().toString();
    }
}
