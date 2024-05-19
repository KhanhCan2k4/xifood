package vn.edu.tdc.xifood.datamodels;

import java.util.HashMap;
import java.util.Map;

public class OrderedProduct {
    private Product product;
    private Map<String, Long> toppings;
    private int amount;
    private Order order; // Thêm trường order vào OrderedProduct

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public Map<String, Long> getToppings() {
        return toppings;
    }

    public void setToppings(Map<String, Long> toppings) {
        this.toppings = toppings;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderedProduct(Product product, int amount) {
        this.product = product;
        this.amount = amount;
        this.toppings = new HashMap<String, Long>();
    }

    public OrderedProduct() {
        this.product = new Product();
        this.amount = 0;
        this.toppings = new HashMap<String, Long>();
    }
}
