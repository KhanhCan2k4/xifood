package vn.edu.tdc.xifood.models;

public class User {
    //field
    private int id;
    private String name;
    private String avatar;
    private String bio;
    private String dayBorn;
    private String email;
    private String gender;
    private String phoneNumber;

    //properties

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDayBorn() {
        return dayBorn;
    }

    public void setDayBorn(String dayBorn) {
        this.dayBorn = dayBorn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User(int id, String name, String avatar, String bio, String dayBorn, String email, String gender, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.bio = bio;
        this.dayBorn = dayBorn;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public User() {
    }
}
