package vn.edu.tdc.xifood.models;

public class Account {
    private String username;
    private String password;
    private String email;
    private int role;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Account() {
        this.username = "";
        this.password = "";
        this.email = "";
        this.role = 0;
    }

    public Account(String username, String password, String email, int role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
