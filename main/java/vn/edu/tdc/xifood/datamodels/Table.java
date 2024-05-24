package vn.edu.tdc.xifood.datamodels;

import androidx.annotation.NonNull;

public class Table {
    private String key;
    private String name;

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

    public Table(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public Table() {
        this.key = "";
        this.name = "";
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
