package vn.edu.tdc.xifood.datamodels;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import vn.edu.tdc.xifood.apis.OrderAPI;

public class Order {
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_DONE = 1;
    public static final int STATUS_CANCEL = 2; // đừng quan tâm (mẹ kêu vậy)
    private String key;
    private ArrayList<OrderedProduct> orderedProducts;
    private String date;
    private int status;
    private User user;

    private int rating;
    private String reviewContent;
    private String imageReview;


    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getImageReview() {
        return imageReview;
    }

    public void setImageReview(String imageReview) {
        this.imageReview = imageReview;
    }

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



    public void addOrderedProduct(OrderedProduct orderedProduct) {
        for (OrderedProduct op : orderedProducts) {
            if (op.getProduct().getKey().equals(orderedProduct.getProduct().getKey())) {
                op.setAmount(op.getAmount() + orderedProduct.getAmount());

                if (op.getAmount() > 5) {
                    op.setAmount(5);
                }
                return;
            }
        }
        orderedProducts.add(orderedProduct);
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
       if (this.status != status){
           this.status = status;
           OrderAPI.update(this);
       }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order() {
        this.rating = 0;
        this.reviewContent = "";
        this.imageReview = "";
    }

    // constructors sao chep
    public Order(Order original) {
        this.user = original.user;
        this.orderedProducts = new ArrayList<>(original.orderedProducts);
        this.status = STATUS_WAITING;
    }

}
