package user;

import java.util.ArrayList;
import java.util.List;

public class PasswordChecker {

    public static List<String> validateStrongPassword(String username, String password){

        List<String> errorMessages = new ArrayList<>();

        if(username.length() < 8){
            errorMessages.add("Username must be at least 8 characters long");
        }

        if (password.length() < 8) {
            errorMessages.add("Password must be at least 8 characters long.");
        }

        if (!password.matches(".*[A-Z].*")) {
            errorMessages.add("Password must contain at least one uppercase letter.");
        }

        if (!password.matches(".*[a-z].*")) {
            errorMessages.add("Password must contain at least one lowercase letter.");
        }

        if (!password.matches(".*[!@#$%^&*()].*")) {
            errorMessages.add("Password must contain at least one special character (!@#$%^&*()).");
        }

        if (!password.matches(".*\\d.*")) {
            errorMessages.add("Password must contain at least one number.");
        }

        return errorMessages;

    }
}