package user;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static final String CSV_FILE_PATH = "data/UserData.csv";
    private Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
        loadUsers();
        addPredefinedLibrarian();
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                    users.put(parts[0], user);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error during reading user data from csv");   //Helpful for debugging
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (User user : users.values()) {
                writer.write(user.getID() + "," + user.getUsername() + "," + user.getPassword() + "," +
                        user.getName() + "," + user.getAddress() + "," + user.getPhoneNumber() + "," + user.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error during writing user data to csv");    //Helpful in case of debugging
        }
    }

    public boolean isUsernameTaken(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void addUser(User user) {
        users.put(user.getID(), user);
        saveUsers();
    }


    public User getUserByUsername(String username) {

        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean validateUser(String username, char[] password) {
        User user = getUserByUsername(username);

        return user != null && user.getPassword().equals(new String (password));
    }

    private void addPredefinedLibrarian(){
        String librarianID = "lib1";
        String librarianUsername = "librarian";
        String librarianPassword = "Lib@1234";
        String librarianName = "Librarian";
        String librarianAddress = "Library Address";
        String librarianPhoneNumber = "0000-00000-0000";
        String librarianEmail = "librarian@library.com";
        User librarian = new User(librarianID, librarianUsername, librarianPassword, librarianName, librarianAddress, librarianPhoneNumber, librarianEmail, UserRole.LIBRARIAN);

        users.put(librarianID, librarian);
    }


}