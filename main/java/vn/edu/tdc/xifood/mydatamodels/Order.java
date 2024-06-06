package vn.edu.tdc.xifood.mydatamodels;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order implements Serializable {
    private String key;
    private ArrayList<OrderedProduct> orderedProducts;
    private String date;
    private String status;
    private String voucher;
    private String payment;
    private String table;
    private String note;
    private String address;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Order() {
        this.address = "";
        this.orderedProducts = new ArrayList<>();
        this.status = "OD_01";
        this.key = "";
        this.note = "";
        this.date = LocalDateTime.now().toString();
        this.table = "";
        this.payment = "CASH_KEY";
        this.voucher = "";
    }

    @Override
    public String toString() {
        return "Order{" +
                "key='" + key + '\'' +
                ", orderedProducts=" + orderedProducts +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", voucher='" + voucher + '\'' +
                ", payment='" + payment + '\'' +
                ", table='" + table + '\'' +
                ", note='" + note + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public static class OrderedProduct extends Cart.InCartProduct {
        private String review;
        private int rating;
        private String reviewingImage;
        private String note;

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getReviewingImage() {
            return reviewingImage;
        }

        public void setReviewingImage(String reviewingImage) {
            this.reviewingImage = reviewingImage;
        }

        public OrderedProduct() {
            super();
            this.review = "";
            this.rating = 1;
            this.reviewingImage = "";
            this.note = "";
        }
    }
}
