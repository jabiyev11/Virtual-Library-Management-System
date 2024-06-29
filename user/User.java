package user;

import java.util.Objects;
import java.util.UUID;

public class User {

    private String ID; // String because of UUID unique ID generation
    private String username;
    private String password;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private UserRole role;


    //for user creation during the signup
    public User(String username, String password, String name, String address, String phoneNumber, String email,  UserRole role) {
        this.ID = generateUniqueID();
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;

    }


    //To load users from csv, we will need ID for that
    public User(String ID, String username, String password, String name, String address, String phoneNumber, String email) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    //For Librarian creation, additional constructor because we do not want to write his credentials in the csv file
    public User(String ID, String username, String password, String name, String address, String phoneNumber, String email, UserRole role) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
    }

    private String generateUniqueID(){
        return UUID.randomUUID().toString();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(ID, user.ID) && Objects.equals(name, user.name) && Objects.equals(address, user.address) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name, address, phoneNumber, email, username, password);
    }

    @Override
    public String toString() {
        return "user.User{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
