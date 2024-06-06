package vn.edu.tdc.xifood.mydatamodels;

public class Review {
    private String key;
    private String userKey;
    private String content;
    private int rating;

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

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public Review() {
        this.key = "";
        this.userKey = "";
        this.content = "";
        this.rating = 1;
    }
}
