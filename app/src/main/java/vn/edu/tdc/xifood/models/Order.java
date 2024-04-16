package vn.edu.tdc.xifood.models;

import java.util.ArrayList;
import java.util.Map;

public class Order {
    private int id;
    private Map<Integer, Integer> amounts;

    private ArrayList<Product> products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(Product... products) {
        this.products.clear();
        for (Product product: products) {
            this.products.add(product);
        }
    }

    public Order(int id) {
        this.id = id;
        this.products = new ArrayList<>();
    }
}
