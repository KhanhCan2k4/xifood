 package vn.edu.tdc.xifood.mydatamodels;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Product implements Serializable {
    private String key;
    private String name;
    private String description;
    private ArrayList<String> image;
    private long price;
    private float rating;
    private int views;
    private int buyTimes;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getImage() {
        return image;
    }
    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public long getPrice() {
        return price;
    }
    public void setPrice(long price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getBuyTimes() {
        return buyTimes;
    }

    public void setBuyTimes(int buyTimes) {
        this.buyTimes = buyTimes;
    }

    public Product() {
        this.key = "";
        this.name = "";
        this.price = 0;
        this.description = "";
        this.image = new ArrayList<String>();
        this.rating = 1.0f;
        this.views = 0;
        this.buyTimes = 0;
    }

    public static String getPriceInFormat(Object price) {
        return new DecimalFormat("#,###,### VND").format(price);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
       Product product = (Product) obj;

       return  this.key.equals(product.key);
    }
}
