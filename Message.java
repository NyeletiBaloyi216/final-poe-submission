/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.login;
 import java.util.Random;

 

/**
 *
 * @author baloy
 */
public class Message {

  
    private String messageID;
    private int numMessagesSent;
    private String recipient;
    private String message;
    private String messageHash;
    private boolean isSent;
    private boolean isStored;
    
    private static int totalMessagesSent = 0;
    private static Random random = new Random();
    
    public Message(int messageNumber) {
        this.numMessagesSent = messageNumber;
        this.messageID = generateMessageID();
        this.isSent = false;
        this.isStored = false;
    }
    
    private String generateMessageID() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }
    
    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() <= 10;
    }
    
    public String checkRecipientCell(String cellNumber) {
        if (cellNumber == null || cellNumber.isEmpty()) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        if (!cellNumber.startsWith("+")) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        if (cellNumber.length() > 13) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        this.recipient = cellNumber;
        return "Cell phone number successfully captured.";
    }
    
    public String validateMessage(String msg) {
        if (msg == null) {
            return "Message exceeds 250 characters by 0; please reduce the size.";
        }
        if (msg.length() > 250) {
            int excess = msg.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
        this.message = msg;
        return "Message ready to send.";
    }
    
    //  MESSAGE HASH
    public String createMessageHash() {
        if (message == null || message.isEmpty()) {
            return "";
        }
        String firstTwo = message.substring(0, 2).toUpperCase();
        String lastTwo = message.substring(message.length() - 2).toUpperCase();
        int length = message.length();
        this.messageHash = firstTwo + lastTwo + length;
        return this.messageHash;
    }
    
    //  SEND MESSAGE 
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                this.isSent = true;
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                this.isStored = true;
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid choice.";
        }
    }
    
    //PRINT MESSAGE  
    public String printMessages() {
        if (messageHash == null) {
            createMessageHash();
        }
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + message;
    }
    
    // STORE MESSAGE 
    public String storeMessage() {
        return String.format(
            "{\"messageID\":\"%s\",\"numMessagesSent\":%d,\"recipient\":\"%s\",\"message\":\"%s\",\"messageHash\":\"%s\",\"isSent\":%b,\"isStored\":%b}",
            messageID, numMessagesSent, recipient, message, messageHash, isSent, isStored
        );
    }
    
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    // using Getters and Setters to access and update private varibles 
    public String getMessageID() { return messageID; }
    public int getNumMessagesSent() { return numMessagesSent; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageHash() { return messageHash; }
    public boolean isSent() { return isSent; }
    public boolean isStored() { return isStored; }
    
    public void setMessageID(String messageID) { this.messageID = messageID; }
    public void setMessage(String message) { this.message = message; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public void setNumMessagesSent(int num) { this.numMessagesSent = num; }
}