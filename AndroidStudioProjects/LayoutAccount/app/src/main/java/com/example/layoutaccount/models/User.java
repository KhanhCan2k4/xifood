package com.example.layoutaccount.models;

public class User {
    public User(String image, String name, String bio, String gender, String dayBorn, String email, String phoneNumber) {
        this.image = image;
        this.name = name;
        this.bio = bio;
        this.gender = gender;
        this.dayBorn = dayBorn;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public User() {
        this.image = "";
        this.name = "";
        this.bio = "";
        this.gender = "";
        this.dayBorn = "";
        this.email = "";
        this.phoneNumber = "";
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String image;
    private String name;
    private String bio;
    private String gender;
    private String dayBorn;
    private String email;
    private String phoneNumber;

}
