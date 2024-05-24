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
    private String table = "";
    private String note="";
    private String address="";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }
    public void setOrderedProducts(ArrayList<OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }
    public void addOrderedProduct(OrderedProduct orderedProduct) {
        for (OrderedProduct op: orderedProducts) {
            if (op.getProduct().getKey().equals(orderedProduct.getProduct().getKey())) {
                op.setAmount(op.getAmount()+ orderedProduct.getAmount());

                if (op.getAmount() > 5) {
                    op.setAmount(5);
                }

                return;
            }
        }
        orderedProducts.add(orderedProduct);
    }
    public void removeOrderedProduct(OrderedProduct product) {
        if (orderedProducts != null) {
            orderedProducts.remove(product);
        }
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

}
