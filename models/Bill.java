package vn.edu.tdc.xifood.models;

public class Bill {
    private String name;
    private  int soLuong;
    private double gia;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public Bill() {
    }

    public Bill(String name, int soLuong, double gia) {
        this.name = name;
        this.soLuong = soLuong;
        this.gia = gia;
    }
}
