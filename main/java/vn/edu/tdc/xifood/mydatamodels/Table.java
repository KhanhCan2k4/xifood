package vn.edu.tdc.xifood.mydatamodels;

import androidx.annotation.NonNull;

public class Table {
    private String key;
    private String name;
    private String status;

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

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Table() {
        this.key = "";
        this.name = "";
        this.status = "";
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
