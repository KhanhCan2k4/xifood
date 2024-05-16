package vn.edu.tdc.xifood.data;

import java.util.ArrayList;

import vn.edu.tdc.xifood.models.Product;

public class ListProductsData {
    private static ArrayList<Product> products;

    public static ArrayList<Product> getProducts(){
        if (products == null){
            products = new ArrayList<Product>();

            Product product1 = new Product(1,"Khanh1","", 19000);
            Product product2 = new Product(2,"Khanh2","", 29000);
            Product product3 = new Product(3,"Khanh3","", 39000);
            Product product4 = new Product(1,"Khanh4","", 49000);
            Product product5 = new Product(2,"Khanh5","", 59000);

            products.add(product1);
            products.add(product2);
            products.add(product3);
            products.add(product4);
            products.add(product5);
        }
        return products;
    }
}
