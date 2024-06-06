package vn.edu.tdc.xifood.mydatamodels;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    private String key;
    private String name;
    private String icon;
    private ArrayList<String> products;

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

    public ArrayList<String> getProducts() {
        return products;
    }
    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public Category() {
       this.key = "";
       this.name = "";
       this.icon = "";
       this.products = new ArrayList<String>();
    }
}
