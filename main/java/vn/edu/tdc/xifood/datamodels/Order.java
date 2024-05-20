package vn.edu.tdc.xifood.datamodels;

import java.util.ArrayList;
import java.util.Map;

public class Order {
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_DONE = 1;
    public static final int STATUS_CANCEL = 2;
    private String key;
    private ArrayList<OrderedProduct> orderedProducts;
    private String date;
    private int status;
    private User user;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }
    public void setOrderedProducts(ArrayList<OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Order() {
    }

    public class OrderedProduct {
        private Product product;
        private ArrayList<Topping> toppings;
        private int amount;

        public Product getProduct() {
            return product;
        }
        public void setProduct(Product product) {
            this.product = product;
        }

        public ArrayList<Topping> getToppings() {
            return toppings;
        }
        public void setToppings(ArrayList<Topping> toppings) {
            this.toppings = toppings;
        }

        public int getAmount() {
            return amount;
        }
        public void setAmount(int amount) {
            this.amount = amount;
        }

        public OrderedProduct(Product product, int amount) {
            this.product = product;
            this.amount = amount;
        }
    }
}
