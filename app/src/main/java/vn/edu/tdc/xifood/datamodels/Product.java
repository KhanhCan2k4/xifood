package vn.edu.tdc.xifood.datamodels;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    private String key;
    private String name;
    private String description;
    private ArrayList<String> image;
    private long price;
    private ArrayList<Category> categories;

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

    public ArrayList<Category> getCategories() {
        return categories;
    }
    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public Product(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public Product() {
        this.name = "";
        this.price = 0;
    }
}
