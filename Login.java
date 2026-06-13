/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.login;
import java.util.regex.Pattern;

/**
 *
 * @author baloy
 */
public class Login {



    private String storedUsername;
    private String storedPassword;
    private String storedCellphone;

    // Username must contain underscore and be <= 5 characters
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // Password validation
    public boolean checkPasswordComplexity(String password) {
        boolean length = password.length() >= 8;
        boolean capital = password.matches(".[A-Z].");
        boolean number = password.matches(".[0-9].");
        boolean special = password.matches(".[!@#$%^&(),.?\":{}|<>].*");

        return length && capital && number && special;
    }

    public boolean checkCellPhoneNumber(String phoneNumber) {
        String regex = "^\\+27[0-9]{9}$";
        return Pattern.matches(regex, phoneNumber);
    }

    // Register user
    public String registerUser(String username, String password, String phoneNumber) {

        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }

        storedUsername = username;
        storedPassword = password;
        storedCellphone = phoneNumber;

        return "User successfully registered.";
    }

    public boolean loginUser(String username, String password) {
        return username.equals(storedUsername) && password.equals(storedPassword);
    }

    // Return login status
    public String returnLoginStatus(boolean loginSuccess, String firstName, String lastName) {

        if (loginSuccess) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}