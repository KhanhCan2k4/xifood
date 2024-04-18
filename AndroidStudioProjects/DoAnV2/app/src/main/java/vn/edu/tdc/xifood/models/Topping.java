package vn.edu.tdc.xifood.models;

public class Topping {
private String name="";
private double price=0;
private int soluong =0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public Topping(String name, double price, int soluong) {
        this.name = name;
        this.price = price;
        this.soluong = soluong;
    }
    public Topping() {

    }
}
