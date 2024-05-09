package vn.edu.tdc.xifood.datamodels;

import java.util.ArrayList;

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

    public Order(Order order) {
        this.key = order.getKey();
        this.orderedProducts = order.getOrderedProducts();
        this.date = order.getDate();
        this.status = order.getStatus();
        this.user = order.getUser();
    }
    public Order() {

    }

    public Order(String key, ArrayList<OrderedProduct> orderedProducts, String date, int status, User user) {
        this.key = key;
        this.orderedProducts = orderedProducts;
        this.date = date;
        this.status = status;
        this.user = user;
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

        public OrderedProduct(Product product, ArrayList<Topping> toppings, int amount) {
            this.product = product;
            this.toppings = toppings;
            this.amount = amount;
        }

//        public OrderedProduct() {
//            this.product = new Product();
//            this.toppings = new ArrayList<>();
//            this.amount = 0;
//        }
        public OrderedProduct() {
            // Constructor mặc định không làm gì cả, nhưng cần phải có để Firebase có thể deserialize dữ liệu.
        }
    }
}
