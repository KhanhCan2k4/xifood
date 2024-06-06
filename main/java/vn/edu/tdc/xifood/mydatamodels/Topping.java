package vn.edu.tdc.xifood.mydatamodels;

public class Topping {
    private String key;
    private String name;
    private long price;

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

    public Topping() {
        this.key = "";
        this.name = "";
        this.price = 0;
    }
}
