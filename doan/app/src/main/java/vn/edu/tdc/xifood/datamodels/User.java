package vn.edu.tdc.xifood.datamodels;

public class User {
    //fields
    private String key;
    private String fullName;
    private String avatar;
    private String dayOfBirth;
    private String gender;
    private String bio;
    private String email;
    private String password;
    private String phoneNumber;
    private int permistion;

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

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
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

    public int getPermistion() {
        return permistion;
    }

    public void setPermistion(int permistion) {
        this.permistion = permistion;
    }

    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.dayOfBirth = "";
        this.bio = "";
        this.key = "";
        this.avatar = "avatars/default.jpg";
        this.phoneNumber = "";
        this.gender = "Unknown";
        this.permistion = 0;
    }

    public User() {
        this.fullName = "";
        this.email = "";
        this.password = "";
        this.dayOfBirth = "";
        this.bio = "";
        this.key = "";
        this.avatar = "avatars/default.jpg";
        this.phoneNumber = "";
        this.gender = "Unknown";
        this.permistion = 0;
    }
}
