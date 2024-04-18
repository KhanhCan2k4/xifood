package vn.edu.tdc.xifood.models;

public class Category {

    private int id;
    private int photo;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category() {
    }

    public Category(int id, int photo, String name) {
        this.id = id;
        this.photo = photo;
        this.name = name;
    }
}
