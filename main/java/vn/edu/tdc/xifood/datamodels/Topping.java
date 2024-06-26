package vn.edu.tdc.xifood.datamodels;

import java.util.ArrayList;

import vn.edu.tdc.xifood.models.Category;

public class Topping {
    private String key;
    private String name;
    private long price;
    private ArrayList<Category> categories;

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

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

    public Topping(String name, long price, boolean selected) {
        this.name = name;
        this.price = price;
        this.selected = selected;
    }
    public Topping() {
        this.name = "";
        this.price = 0;
        this.selected = false;
    }
}
