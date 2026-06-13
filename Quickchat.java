/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.login;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


public class Quickchat {
    // STORES ALL MESSAGES THAT WRE SUCCESFULLY SENT OR SAVED
private static ArrayList<Message> sentMessagesList = new ArrayList<>();

// ARRAYS USED TO KEEP TRACK OF DIFFERENT MESSAGE CATEGORIES
    private static String[] sentMessagesArray = new String[10];
    private static String[] disregardedMessagesArray = new String[10];
    private static String[] storedMessagesArray = new String[10];
    private static String[] messageHashArray = new String[10];
    private static String[] messageIDArray = new String[10];
    private static String[] storedRecipientsArray = new String[10];

    
    //COUNTERS HELPS US KNOW HOW MANY VALID ENTRIES ARE CURRENTLY IN ECH ARRAY
    private static int sentCount = 0;
    private static int disregardedCount = 0;
    private static int storedCount = 0;

    private static final String JSON_FILE = "stored_messages.json";

    public static void startMessaging(Scanner scanner, Login user) {

        System.out.println("Welcome to QuickChat.");

        System.out.print("\nHow many messages do you wish to send? ");
        int totalMessages = Integer.parseInt(scanner.nextLine());

        if (totalMessages <= 0) {
            System.out.println("No messages to send.");
            populateTestData();
            readStoredMessagesFromJSON();
            storedMessagesMenu(scanner);
            return;
        }

        int messagesDone = 0;
        boolean running = true;

        while (running && messagesDone < totalMessages) {

            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1) Send Message");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {

                System.out.println("\n--- Message " + (messagesDone + 1) + " of " + totalMessages + " ---");

                Message msg = new Message(messagesDone);

                System.out.println("Message ID: " + msg.getMessageID());

                //  keep asking until the user enters a valid cellnumber
                String recipientResult;
                do {
                    System.out.print("Enter recipient cell number (+ and max 10 chars): ");
                    String cell = scanner.nextLine();
                    recipientResult = msg.checkRecipientCell(cell);
                    System.out.println(recipientResult);
                } while (!recipientResult.equals("Cell phone number successfully captured."));

                //  ensure the message does not exceed allowed character limit
                String messageResult;
                do {
                    System.out.print("Enter message (max 250 chars): ");
                    String text = scanner.nextLine();
                    messageResult = msg.validateMessage(text);
                    System.out.println(messageResult);
                } while (!messageResult.equals("Message ready to send."));

                // generate a unique hash that can later be used fpr searcching or deleting messages
                System.out.println("Message Hash: " + msg.createMessageHash());

                // show the user what the message will look like before savinng the action
                System.out.println("\n--- Preview ---");
                System.out.println(msg.printMessages());

                // user decides to send,disregard or store the message
                System.out.println("\n1) Send");
                System.out.println("2) Disregard");
                System.out.println("3) Store for later");
                System.out.print("Choose: ");

                int action = Integer.parseInt(scanner.nextLine());

                System.out.println(msg.sentMessage(action));

                // save the message into the correct array because of the selection option
                if (action == 1) {
                    sentMessagesList.add(msg);
                    addToSentArray(msg);
                    messagesDone++;
                } else if (action == 2) {
                    addToDisregardedArray(msg);
                    messagesDone++;
                } else if (action == 3) {
                    sentMessagesList.add(msg);
                    addToStoredArray(msg);
                    messagesDone++;
                }

            } else if (choice == 2) {

                System.out.println("\n--- Sent Messages ---");

                if (sentMessagesList.isEmpty()) {
                    System.out.println("No messages stored.");
                } else {
                    for (Message m : sentMessagesList) {
                        System.out.println(m.printMessages());
                        System.out.println("----------------------");
                    }
                }

            } else if (choice == 3) {

                running = false;
                System.out.println("Thank you for using QuickChat. Goodbye!");

            } else {

                System.out.println("Invalid option. Try again.");
            }
        }

        System.out.println("\n================================");
        System.out.println("Total messages sent: " + Message.returnTotalMessages());
        System.out.println("================================");

                populateTestData();
        readStoredMessagesFromJSON();
        storedMessagesMenu(scanner);
    }

    public static void populateTestData() {
        addTestMessage("+27834557896", "Did you get the cake?", "Sent", "MSG001");
        addTestMessage("+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored", "MSG002");
        addTestMessage("+27834484567", "Yohoooo, I am at your gate.", "Disregard", "MSG003");
        addTestMessage("0838884567", "It is dinner time", "Sent", "MSG004");
        addTestMessage("+27838884567", "Ok, I am leaving without you.", "Stored", "MSG005");
    }

    private static void addTestMessage(String recipient, String message, String flag, String id) {
        String hash = createSimpleHash(message);

        if (flag.equalsIgnoreCase("Sent")) {
            if (sentCount < sentMessagesArray.length) {
                sentMessagesArray[sentCount] = message;
                sentCount++;
            }
        } else if (flag.equalsIgnoreCase("Disregard")) {
            if (disregardedCount < disregardedMessagesArray.length) {
                disregardedMessagesArray[disregardedCount] = message;
                disregardedCount++;
            }
        } else if (flag.equalsIgnoreCase("Stored")) {
            if (storedCount < storedMessagesArray.length) {
                storedMessagesArray[storedCount] = message;
                storedRecipientsArray[storedCount] = recipient;
                messageHashArray[storedCount] = hash;
                messageIDArray[storedCount] = id;
                storedCount++;
            }
        }
    }

    private static String createSimpleHash(String message) {
        if (message == null || message.length() < 2) return "";
        String firstTwo = message.substring(0, 2).toUpperCase();
        String lastTwo = message.substring(message.length() - 2).toUpperCase();
        int length = message.length();
        return firstTwo + lastTwo + length;
    }

    public static void addToSentArray(Message msg) {
        if (sentCount < sentMessagesArray.length) {
            sentMessagesArray[sentCount] = msg.getMessage();
            sentCount++;
        }
        
    }

    public static void addToDisregardedArray(Message msg) {
        if (disregardedCount < disregardedMessagesArray.length) {
            disregardedMessagesArray[disregardedCount] = msg.getMessage();
            disregardedCount++;
        }
        
    }

    public static void addToStoredArray(Message msg) {
        if (storedCount < storedMessagesArray.length) {
            storedMessagesArray[storedCount] = msg.getMessage();
            storedRecipientsArray[storedCount] = msg.getRecipient();
            messageHashArray[storedCount] = msg.getMessageHash();
            messageIDArray[storedCount] = msg.getMessageID();
            storedCount++;
        }
    }

    public static void readStoredMessagesFromJSON() {
        
        try {
            String content = new String(Files.readAllBytes(Paths.get(JSON_FILE)));
            
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                if (storedCount >= storedMessagesArray.length) {
                    expandStoredArrays();
                }

                storedMessagesArray[storedCount] = obj.getString("message");
                storedRecipientsArray[storedCount] = obj.getString("recipient");
                messageHashArray[storedCount] = obj.getString("hash");
                messageIDArray[storedCount] = obj.getString("id");
                storedCount++;
            }
            System.out.println("JSON file loaded successfully.");
        } catch (IOException e) {
            System.out.println("Note: JSON file not found. Using test data only.");
        }
    }

    private static void expandStoredArrays() {
        int newSize = storedMessagesArray.length * 2;
        storedMessagesArray = expandArray(storedMessagesArray, newSize);
        storedRecipientsArray = expandArray(storedRecipientsArray, newSize);
        messageHashArray = expandArray(messageHashArray, newSize);
        messageIDArray = expandArray(messageIDArray, newSize);
    }

    private static String[] expandArray(String[] arr, int newSize) {
        String[] newArr = new String[newSize];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        return newArr;
    }
    
    

    public static void storedMessagesMenu(Scanner scanner) {
        boolean inSubMenu = true;

        while (inSubMenu) {
            
            System.out.println("\n===== STORED MESSAGES =====");
            System.out.println("a. Display the sender and recipient of all stored messages");
            System.out.println("b. Display the longest stored message");
            System.out.println("c. Search for a message ID and display the corresponding recipient and message");
            System.out.println("d. Search for all the messages stored for a particular recipient");
            System.out.println("e. Delete a message using the message hash");
            System.out.println("f. Display a report that lists the full details of all the stored messages");
            System.out.println("x. Return to main menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().toLowerCase();

            switch (choice) {
                case "a":
                    displayStoredSendersRecipients();
                    break;
                case "b":
                    displayLongestStoredMessage();
                    break;
                case "c":
                    searchMessageID(scanner);
                    break;
                case "d":
                    searchRecipientMessages(scanner);
                    break;
                case "e":
                    deleteMessageByHash(scanner);
                    break;
                case "f":
                    displayFullReport();
                    break;
                case "x":
                    inSubMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    //  Display the sender and recipient of all stored messages
    public static void displayStoredSendersRecipients() {
        System.out.println("\n--- Stored Messages: Senders and Recipients ---");
        if (storedCount == 0) {
            System.out.println("No stored messages.");
            return;
        }
        for (int i = 0; i < storedCount; i++) {
            System.out.println("Message ID: " + messageIDArray[i]);
            System.out.println("Recipient: " + storedRecipientsArray[i]);
            System.out.println("Message: " + storedMessagesArray[i]);
            System.out.println("--------------------");
        }
    }

    //  Display the longest stored message
    public static String displayLongestStoredMessage() {
        if (storedCount == 0) {
            System.out.println("No stored messages.");
            return "";
        }

        int maxLength = 0;
        int maxIndex = 0;

        for (int i = 0; i < storedCount; i++) {
            if (storedMessagesArray[i] != null && storedMessagesArray[i].length() > maxLength) {
                maxLength = storedMessagesArray[i].length();
                maxIndex = i;
            }
        }

        String longest = storedMessagesArray[maxIndex];
        System.out.println("\n--- Longest Stored Message ---");
        System.out.println("Message: " + longest);
        System.out.println("Length: " + maxLength + " characters");
        System.out.println("Recipient: " + storedRecipientsArray[maxIndex]);
        return longest;
    }

    public static String searchMessageID(Scanner scanner) {
        System.out.print("\nEnter Message ID to search (e.g., MSG002): ");
        String searchID = scanner.nextLine();
        return searchMessageID(searchID);
    }

    public static String searchMessageID(String searchID) {
        for (int i = 0; i < storedCount; i++) {
            if (messageIDArray[i] != null && messageIDArray[i].equals(searchID)) {
                String result = "Recipient: " + storedRecipientsArray[i] +
                               "\nMessage: " + storedMessagesArray[i];
                System.out.println("\n--- Message Found ---");
                System.out.println(result);
                return storedMessagesArray[i];
            }
        }
        System.out.println("Message ID not found.");
        return null;
    }

    public static String[] searchRecipientMessages(Scanner scanner) {
        System.out.print("\nEnter recipient to search (e.g., +27838884567): ");
        String searchRecipient = scanner.nextLine();
        return searchRecipientMessages(searchRecipient);
    }

    public static String[] searchRecipientMessages(String searchRecipient) {
        int matchCount = 0;
        for (int i = 0; i < storedCount; i++) {
            if (storedRecipientsArray[i] != null && storedRecipientsArray[i].equals(searchRecipient)) {
                matchCount++;
            }
        }

        String[] foundMessages = new String[matchCount];
        int index = 0;

        System.out.println("\n--- Messages for " + searchRecipient + " ---");
        for (int i = 0; i < storedCount; i++) {
            if (storedRecipientsArray[i] != null && storedRecipientsArray[i].equals(searchRecipient)) {
                foundMessages[index] = storedMessagesArray[i];
                System.out.println("- " + storedMessagesArray[i]);
                index++;
            }
        }

        if (matchCount == 0) {
            System.out.println("No stored messages found for this recipient.");
        }
        return foundMessages;
    }

    public static boolean deleteMessageByHash(Scanner scanner) {
        System.out.print("\nEnter message hash to delete: ");
        String hash = scanner.nextLine();
        return deleteMessageByHash(hash);
    }

    public static boolean deleteMessageByHash(String hash) {
        for (int i = 0; i < storedCount; i++) {
            if (messageHashArray[i] != null && messageHashArray[i].equals(hash)) {
                String deletedMsg = storedMessagesArray[i];

                for (int j = i; j < storedCount - 1; j++) {
                    storedMessagesArray[j] = storedMessagesArray[j + 1];
                    storedRecipientsArray[j] = storedRecipientsArray[j + 1];
                    messageHashArray[j] = messageHashArray[j + 1];
                    messageIDArray[j] = messageIDArray[j + 1];
                }

                storedMessagesArray[storedCount - 1] = null;
                storedRecipientsArray[storedCount - 1] = null;
                messageHashArray[storedCount - 1] = null;
                messageIDArray[storedCount - 1] = null;
                storedCount--;

                System.out.println("Message: \"" + deletedMsg + "\" successfully deleted.");
                return true;
            }
        }
        System.out.println("Message hash not found.");
        return false;
    }

    public static void displayFullReport() {
        System.out.println("\n========== FULL STORED MESSAGES REPORT ==========");
        System.out.println("Total Stored Messages: " + storedCount);
        System.out.println("=================================================");

        for (int i = 0; i < storedCount; i++) {
            System.out.println("Message Hash: " + messageHashArray[i]);
            System.out.println("Recipient:    " + storedRecipientsArray[i]);
            System.out.println("Message:      " + storedMessagesArray[i]);
            System.out.println("Message ID:   " + messageIDArray[i]);
            System.out.println("-------------------------------------------------");
        }
    }

    // getter methods used by unit tests toverify that arrays have the correct information
    public static String[] getSentMessagesArray() {
        String[] result = new String[sentCount];
        System.arraycopy(sentMessagesArray, 0, result, 0, sentCount);
        return result;
    }
    public static String[] getDisregardedMessagesArray() {
        String[] result = new String[disregardedCount];
        System.arraycopy(disregardedMessagesArray, 0, result, 0, disregardedCount);
        return result;
    }
    public static String[] getStoredMessagesArray() {
        String[] result = new String[storedCount];
        System.arraycopy(storedMessagesArray, 0, result, 0, storedCount);
        return result;
    }
    public static String[] getMessageHashArray() {
        String[] result = new String[storedCount];
        System.arraycopy(messageHashArray, 0, result, 0, storedCount);
        return result;
    }
    public static String[] getMessageIDArray() {
        String[] result = new String[storedCount];
        System.arraycopy(messageIDArray, 0, result, 0, storedCount);
        return result;
    }
    public static String[] getStoredRecipientsArray() {
        String[] result = new String[storedCount];
        System.arraycopy(storedRecipientsArray, 0, result, 0, storedCount);
        return result;
    }
    public static int getSentCount() { return sentCount; }
    public static int getDisregardedCount() { return disregardedCount; }
    public static int getStoredCount() { return storedCount; }

    // ========== RESET FOR TESTING ==========
    public static void resetArrays() {
        sentMessagesArray = new String[10];
        disregardedMessagesArray = new String[10];
        storedMessagesArray = new String[10];
        messageHashArray = new String[10];
        messageIDArray = new String[10];
        storedRecipientsArray = new String[10];
        sentCount = 0;
        disregardedCount = 0;
        storedCount = 0;
        sentMessagesList.clear();
    }
}