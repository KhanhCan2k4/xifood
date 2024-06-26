package vn.edu.tdc.xifood.models;

public class Product {
    private String key;
    private int id;
    private String name;
    private String description;
    private String image;

    private int price;
    private int amount;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amount <= 0 ) {
            this.amount = 0;
        } else {
            this.amount = amount;
        }
    }

    public Product(int id, String name, String image, int price) {
        this.id = id;
        this.name = name;
        this.description = "";
        this.image = image;
        this.amount = 0;
        this.price = price;
    }
}
