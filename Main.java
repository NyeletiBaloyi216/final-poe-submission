/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.login;
   import java.util.Scanner;

/**
 *
 * @author baloy
 */
 
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Welcome to QuickChat System ===");

        // Create login 
        Login user = new Login();

        //  Registration 
        System.out.println("\n=== LOGIN / REGISTER ===");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter cellphone (+27...): ");
        String phone = scanner.nextLine();

        String registerResult = user.registerUser(username, password, phone);
        System.out.println(registerResult);

        if (!registerResult.equals("User successfully registered.")) {
            System.out.println("Exiting program...");
            return;
        }

        //  Login attempt
        System.out.println("\n=== LOGIN ===");

        System.out.print("Enter username: ");
        String loginUser = scanner.nextLine();

        System.out.print("Enter password: ");
        String loginPass = scanner.nextLine();

        boolean loginSuccess = user.loginUser(loginUser, loginPass);

        System.out.println(
            user.returnLoginStatus(loginSuccess, "User", "")
        );
       // if user fails to login . exit program 
        if (!loginSuccess) {
            System.out.println("Exiting program...");
            return;
        }

        //  Start messaging system
        Quickchat.startMessaging(scanner, user);

        scanner.close();
    }
    
}
