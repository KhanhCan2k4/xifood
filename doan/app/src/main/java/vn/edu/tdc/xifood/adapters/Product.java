package vn.edu.tdc.xifood.adapters;

public class Product {
    private String imageProduct;
    private String nameProduct;
    private int priceProduct;

    public Product() {
        this.imageProduct = "";
        this.nameProduct = "";
        this.priceProduct = 0;
    }

    public Product(String imageProduct, String nameProduct, int priceProduct) {
        this.imageProduct = imageProduct;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }
}
