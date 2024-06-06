package vn.edu.tdc.xifood.mydatamodels;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    //fields
    private String key;
    private String fullName;
    private String dayOfBirth;
    private String gender;
    private String email;
    private String password;
    private String phoneNumber;
    private String permistion;
    private ArrayList<String> address;

    public ArrayList<String> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<String> address) {
        this.address = address;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }
    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPermistion() {
        return permistion;
    }

    public void setPermistion(String permistion) {
        this.permistion = permistion;
    }

    public User() {
        this.fullName = "";
        this.email = "";
        this.password = "";
        this.dayOfBirth = "";
        this.key = "";
        this.phoneNumber = "";
        this.gender = "US_DE";
        this.address = new ArrayList<>();
        this.permistion = "PM_02";
    }
}
