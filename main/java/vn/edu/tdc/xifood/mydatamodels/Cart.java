package vn.edu.tdc.xifood.mydatamodels;

import java.io.Serializable;
import java.util.ArrayList;

import vn.edu.tdc.xifood.SharePreference;

public class Cart {
    private String key;
    private String userKey;
    private ArrayList<InCartProduct> products;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public ArrayList<InCartProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<InCartProduct> products) {
        this.products = products;
    }

    public Cart() {
        this.userKey = SharePreference.find(SharePreference.USER_TOKEN_KEY);
        this.products = new ArrayList<>();
    }

    public static class InCartProduct implements Serializable {
        protected String key;
        protected int quantity;
        protected ArrayList<String> toppings;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public ArrayList<String> getToppings() {
            return toppings;
        }

        public void setToppings(ArrayList<String> toppings) {
            this.toppings = toppings;
        }

        public InCartProduct() {
            this.key = "";
            this.quantity = 0;
            this.toppings = new ArrayList<>();
        }
    }
}
