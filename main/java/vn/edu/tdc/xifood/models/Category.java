package vn.edu.tdc.xifood.models;

import java.util.ArrayList;

public class Category {

    ArrayList<Product> products;

    private int id;
    private int photo;
    private String name;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category() {
    }

    public Category(int id, int photo, String name) {
        this.id = id;
        this.photo = photo;
        this.name = name;
    }
}
