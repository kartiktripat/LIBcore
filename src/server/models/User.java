package server.models;

import server.models.enums.UserRole;
import java.io.Serializable;

public class User implements Serializable {
    private String userID;
    private String username;
    private String password;
    private String address;
    private String contactNumber;
    private UserRole role;

    public User(String userID, String username, String password, String address, String contactNumber, UserRole role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.address = address;
        this.contactNumber = contactNumber;
        this.role = role;
    }

    public boolean validatePassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    // Getters and Setters
    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }
    public String getContactNumber() { return contactNumber; }
    public UserRole getRole() { return role; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setAddress(String address) { this.address = address; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setRole(UserRole role) { this.role = role; }
}
