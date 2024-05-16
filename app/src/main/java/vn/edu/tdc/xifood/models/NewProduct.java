package vn.edu.tdc.xifood.models;

public class NewProduct {

    private int id;
    private int photo;
    private String name;
    private int price;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public NewProduct(int id, int photo, String name, int price) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.price = price;
    }

    public NewProduct() {
    }
}
