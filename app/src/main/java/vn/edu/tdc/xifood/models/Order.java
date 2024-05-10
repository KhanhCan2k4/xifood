package vn.edu.tdc.xifood.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Order {
    private String key;
    private int id;
    private Map<Integer, Integer> amounts;

    private final ArrayList<Product> products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(Product... products) {
        this.products.clear();
        Collections.addAll(this.products, products);
    }

    public Order(int id) {
        this.id = id;
        this.products = new ArrayList<>();
    }
}
