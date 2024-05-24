package vn.edu.tdc.xifood.datamodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderedProduct {
    private Product product;
    private Map<String, Long> toppings;
    private int amount;
    private Order order; // Thêm trường order vào OrderedProduct
    private boolean isCheckedPay = false;

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

    public boolean isCheckedPay() {
        return isCheckedPay;
    }

    public void setCheckedPay(boolean checkedPay) {
        isCheckedPay = checkedPay;
    }

    public OrderedProduct(Product product, int amount, boolean isCheckedPay) {
        this.product = product;
        this.amount = amount;
        this.toppings = new HashMap<String, Long>();
        this.isCheckedPay = isCheckedPay;
    }

    public OrderedProduct() {
        this.product = new Product();
        this.amount = 0;
        this.toppings = new HashMap<String, Long>();
        this.isCheckedPay = false;
    }
    public void mergeToppings(Map<String, Long> newToppings) {
        if (this.toppings == null) {
            this.toppings = new HashMap<>();
        }
        for (Map.Entry<String, Long> newTopping : newToppings.entrySet()) {
            String key = newTopping.getKey();
            Long value = newTopping.getValue();
            this.toppings.merge(key, value, Long::sum);
        }
    }
    public void mergeProduct(OrderedProduct newProduct) {
        // Merge toppings
        mergeToppings(newProduct.getToppings());

        // Merge amounts with a cap at 5
        this.amount = Math.min(this.amount + newProduct.getAmount(), 5);
    }
}
