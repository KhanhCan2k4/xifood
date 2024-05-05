package vn.edu.tdc.xifood.datamodels;

import java.util.ArrayList;

public class Category {
    private String key;
    private String name;
    private String icon;
    private ArrayList<Product> products;
    private ArrayList<Topping> toppings;

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

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Topping> getToppings() {
        return toppings;
    }
    public void setToppings(ArrayList<Topping> toppings) {
        this.toppings = toppings;
    }

    public Category(String name) {
        this.name = name;
    }
}
