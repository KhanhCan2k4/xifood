package vn.edu.tdc.xifood.datamodels;

import androidx.annotation.NonNull;

public class Payment {
    private String owner;
    private String code;
    private String type;
    private String qr;
    private long total;

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getQr() {
        return qr;
    }
    public void setQr(String qr) {
        this.qr = qr;
    }

    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }

    public Payment() {
        this.owner = "";
        this.code = "";
        this.type = "";
        this.qr = "";
        this.total = 0l;
    }

    @NonNull
    @Override
    public String toString() {
        String re = "";
        re += String.format("Tên tài khoản:\t %s\n", this.owner);
        re += String.format("Số tài khoản: \t %s\n", this.code);
        re += String.format("Phương thức:  \t %s\n", this.type);
        re += String.format("Thành tiền:   \t %s\n", Product.getPriceInFormat(this.total));

        return re;
    }
}
