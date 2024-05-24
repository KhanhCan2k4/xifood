package vn.edu.tdc.xifood.models;

public class Cart {
    private  String name;
    private String thuTuSanPham;
    private  int hinhAnh;
    private String toppingThem;
    private double gia;
    private int soLuong;
    private String ghiChu;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThuTuSanPham() {
        return thuTuSanPham;
    }

    public void setThuTuSanPham(String thuTuSanPham) {
        this.thuTuSanPham = thuTuSanPham;
    }

    public int getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(int hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getToppingThem() {
        return toppingThem;
    }

    public void setToppingThem(String toppingThem) {
        this.toppingThem = toppingThem;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Cart(String name, String thuTuSanPham, int hinhAnh, String toppingThem, double gia, int soLuong, String ghiChu) {
        this.name = name;
        this.thuTuSanPham = thuTuSanPham;
        this.hinhAnh = hinhAnh;
        this.toppingThem = toppingThem;
        this.gia = gia;
        this.soLuong = soLuong;
        this.ghiChu = ghiChu;
    }

    public Cart() {
    }
}
