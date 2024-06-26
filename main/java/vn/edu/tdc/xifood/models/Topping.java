package vn.edu.tdc.xifood.models;

public class Topping {
    private String key;
    private String name = "";
    private double price = 0;
    private int soluong = 0;
    public OnToppingClickListener toppingClickListener;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public Topping(String name, double price, int soluong) {
        this.name = name;
        this.price = price;
        this.soluong = soluong;
    }

    public Topping() {

    }

    // Hàm ủy quyền onClickListener cho Topping
    public void setOnToppingClickListener(OnToppingClickListener listener) {
        this.toppingClickListener = listener;
    }

    // Hàm trigger khi Topping được nhấn
    public void triggerClick() {
        if (toppingClickListener != null) {
            toppingClickListener.onToppingClick(this);
        }
    }

    // Interface cho việc xử lý sự kiện khi Topping được nhấn
    public interface OnToppingClickListener {
        void onToppingClick(Topping topping);
    }
}
