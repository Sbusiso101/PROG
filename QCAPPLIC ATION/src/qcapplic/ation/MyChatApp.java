/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qcapplic.ation;

/**
 *
 * @author Sbusiso
 */ 
 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javax.swing.JOptionPane;
import java.io.IOException; 
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyChatApp {
     
    String registeredUser;
    String registeredPassword;
    String registeredName;
    String registeredSurname;
    String registeredCellNumber;

    // New field to store the name of the current recipient for display purposes
    String currentRecipientName;

    // --- MESSAGE STORAGE & MANAGEMENT ---

    // List for disregarded messages
    ArrayList<String> disregardMessage = new ArrayList<>();

    // Updated Lists for Stored Messages (messages saved but not sent)
    ArrayList<String> storedMessageContent = new ArrayList<>();
    ArrayList<String> storedMessageID = new ArrayList<>();
    ArrayList<String> storedHashID = new ArrayList<>();

    // Corrected/Repurposed Lists for Sent Messages (must be parallel lists for indexing)
    ArrayList<String> sentMessage = new ArrayList<>();
    ArrayList<String> sentRecipientPhone = new ArrayList<>(); // Stores the recipient number for EACH sent message
    ArrayList<String> hashID = new ArrayList<>();
    // uniqueMessageID is now an ArrayList for indexing AND stores the IDs
    ArrayList<String> sentMessageID = new ArrayList<>(); 
    ArrayList<String> externalData = new ArrayList<>();
   
    java.util.HashSet<String> uniqueIDTracker = new java.util.HashSet<>(); 

    Random random = new Random();

    // Accumulators
    private int totalMessagesSentAccumulator = 0;
    private int totalMessagesStoredAccumulator = 0; // New accumulator for stored messages

   
    private boolean checkCapitalizedInput(String input, String fieldName) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        char firstChar = input.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                fieldName + " must start with a capital letter.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void registerUser() {
        String userName;
        String userPassword;
        String userCellNumber;
        String name;
        String surname;

        // 1. Get and Validate Name (Must start with a capital letter)
        do {
            name = JOptionPane.showInputDialog("Enter your Name. Must start with a capital letter.");
            if (name == null)
                return;
        } while (!checkCapitalizedInput(name, "Name"));

        // 2. Get and Validate Surname (Must start with a capital letter)
        do {
            surname = JOptionPane.showInputDialog("Enter your Surname. Must start with a capital letter.");
            if (surname == null)
                return;
        } while (!checkCapitalizedInput(surname, "Surname"));

        // 3. Get and Validate Username (Original validation - assuming Validation.checkUsername exists)
        do {
            userName = JOptionPane.showInputDialog("Enter your username. Please ensure that your username contains an underscore and is no more than five characters in length.");
            if (userName == null)
                return;
        } while (!Validation.checkUsername(userName));

        // 4. Get and Validate Password (Original validation - assuming Validation.checkPassword exists)
        do {
            userPassword = JOptionPane.showInputDialog("Enter your password. Please ensue thatit is at least 8 characters and contain a capital letter, a lowercase letter, a number, and a special character");
            if (userPassword == null)
                return;
        } while (!Validation.checkPassword(userPassword));

        // 5. Get and Validate Cell Number (Original validation - assuming Validation.checkCellPhoneNumber exists)
        do {
            userCellNumber = JOptionPane.showInputDialog("Enter your cellphone number.Please use +27XXXXXXXXX or 0XXXXXXXXX");
            if (userCellNumber == null)
                return;
        } while (!Validation.checkCellPhoneNumber(userCellNumber));

        // Store all registered details
        registeredName = name;
        registeredSurname = surname;
        registeredUser = userName;
        registeredPassword = userPassword;
        registeredCellNumber = userCellNumber;

        JOptionPane.showMessageDialog(null, "Registration successful! Welcome, " + registeredName + " " + registeredSurname + ". You can now log in.");
    }

    public void signIn() {
        if (registeredUser == null || registeredPassword == null) {
            JOptionPane.showMessageDialog(null, "You need to register before signing in.");
            return;
        }

        String userName = JOptionPane.showInputDialog("Enter your username to login! Please ensure that your username contains an underscore and is no more than five characters in length ");
        String userPassword = JOptionPane.showInputDialog("Enter your password to login!");

        if (userName == null || userPassword == null) {
            return;
        }

        if (userName.equals(registeredUser) && userPassword.equals(registeredPassword)) {
            String welcomeMessage = "Welcome to ChatBot, " + registeredName + "!";
            String[] options = {"Send message", "Message Management", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, welcomeMessage, "Options",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0 -> sendMessage();
                case 1 -> messageManagement();
                case 2, JOptionPane.CLOSED_OPTION -> System.exit(0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect credentials. Try again!");
        }
    }

    public void sendMessage() {
        String recipientNumber;
        do {
            recipientNumber = JOptionPane.showInputDialog("Enter recipient's number. Please use +27XXXXXXXXX or 0XXXXXXXXX");
            if (recipientNumber == null)
                return;
        } while (!Validation.checkCellPhoneNumber(recipientNumber));

        // Ask for Recipient's Name
        String nameInput = JOptionPane.showInputDialog("Enter recipient's Name:");
        if (nameInput == null) {
            return;
        }
        currentRecipientName = nameInput;

        // Note: The original `recipientPhone` list is redundant now, using `sentRecipientPhone` instead.
        // Clearing/adding to recipientPhone list is now removed.

        String messageNumber = JOptionPane.showInputDialog("How many messages would you like to send?");
        if (messageNumber == null)
            return;

        int sentCount = 0;
        int storedCount = 0;

        try {
            int messageCount = Integer.parseInt(messageNumber);
            String recipientFullName = currentRecipientName + " (" + recipientNumber + ")";

            for (int i = 0; i < messageCount; i++) {
                String message;
                do {
                    message = JOptionPane.showInputDialog(
                                String.format("Enter your message (%d of %d). Max 250 characters:", i + 1, messageCount));
                    if (message == null)
                        return;

                    if (message.length() > 250) {
                        JOptionPane.showMessageDialog(null,
                                "Please enter a message of less than 250 characters.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Message accepted");
                    }

                } while (message.length() > 250);

                String[] options = {"Send", "Store", "Disregard"};
                int action = JOptionPane.showOptionDialog(null, "Choose an action:", "Message Options",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);


                switch (action) {
                    case 0 -> { // SEND
                        String id = createMessageId();
                        totalMessagesSentAccumulator++;
                        String hash = createMessageHash(id, totalMessagesSentAccumulator, message);

                        // Store sent message components in parallel lists
                        sentMessage.add(message);
                        sentMessageID.add(id); // Using the new ArrayList
                        sentRecipientPhone.add(recipientNumber); // Storing the recipient number
                        hashID.add(hash);
                        sentCount++;

                        // Display Sent Message Details in required format (Name + Number)
                        String displayMessage = String.format(
                                "*** MESSAGE SENT SUCCESSFULLY ***\n\n" +
                                "MessageID: %s\n" +
                                "Message Hash: %s\n" +
                                "Recipient: %s\n" +
                                "Message: %s",
                                id, hash, recipientFullName, message
                        );

                        JOptionPane.showMessageDialog(null, displayMessage, "Message Sent Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                    case 1 -> { // STORE - Generating temporary ID/Hash to match required viewing format
                        String id = createMessageId();
                        totalMessagesStoredAccumulator++;
                        String hash = createMessageHash(id, totalMessagesStoredAccumulator, message);

                        // Store message components for later viewing in format
                        storedMessageContent.add(message);
                        storedMessageID.add(id);
                        storedHashID.add(hash);
                        storedCount++;

                        // Display Stored Message Details in required format (Name + Number)
                        String displayMessage = String.format(
                                "*** MESSAGE STORED SUCCESSFULLY ***\n\n" +
                                "MessageID (Temp): %s\n" +
                                "Message Hash (Temp): %s\n" +
                                "Recipient (Intended): %s\n" +
                                "Message: %s",
                                id, hash, recipientFullName, message
                        );

                        JOptionPane.showMessageDialog(null, displayMessage, "Message Stored Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                    case 2 -> { // DISREGARD
                        disregardMessage.add(message);
                        JOptionPane.showMessageDialog(null, "Message disregarded.");
                    }
                }
            }

            // Display total messages sent/stored summary
            JOptionPane.showMessageDialog(null,
                "Message sending session complete.\n" +
                "Total messages sent in this session: " + sentCount + "\n" +
                "Total messages stored in this session: " + storedCount + "\n" +
                "TOTAL MESSAGES SENT ACCUMULATED: " + totalMessagesSentAccumulator,
                "Session Summary", JOptionPane.INFORMATION_MESSAGE);

            // Post-session message viewing prompt
            if (sentCount > 0 || storedCount > 0) {
                String[] viewOptions = {"View Sent Messages", "View Stored Messages","Message management", "Continue"};
                int choice = JOptionPane.showOptionDialog(null, "Messages were sent/stored. Would you like to view them now?", "View Options", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, viewOptions, viewOptions[0]);

                switch (choice) {
                    case 0 -> printSentMessages();
                    case 1 -> printStoredMessages(); 
                    case 2 -> messageManagement();
                    case 3, JOptionPane.CLOSED_OPTION -> { /* return to sign-in loop */ }
                }
            }


        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Allows the user to select and view sent or stored messages (from main menu).
     */
    public void viewMessages() {
        String[] viewOptions = {"View Sent Messages", "View Stored Messages", "View Disregarded Messages", "Back to Menu"};
        int choice = JOptionPane.showOptionDialog(null,
            "What would you like to view?",
            "View Message Options",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            viewOptions,
            viewOptions[0]);

        switch (choice) {
            case 0 -> printSentMessages();
            case 1 -> printStoredMessages();
            case 2 -> printDisregardedMessages();
            case 3, JOptionPane.CLOSED_OPTION -> { /* return to sign-in loop */ }
        }
    }

    /**
     * Prints all sent messages in the required format: MessageID, Message Hash, Recipient, Message.
     */
    public void printSentMessages() {
        if (sentMessage.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages have been sent yet.");
            return;
        }

        StringBuilder display = new StringBuilder("*** SENT MESSAGES ***\n");

        for (int i = 0; i < sentMessage.size(); i++) {
            String recipientNumber = sentRecipientPhone.get(i); // Use the correct parallel list
            String recipientFullName = (currentRecipientName == null || currentRecipientName.isEmpty())
                                     ? recipientNumber
                                     : (currentRecipientName + " (" + recipientNumber + ")");

            String uniqueId = sentMessageID.get(i);
            String hash = hashID.get(i);
            String messageContent = sentMessage.get(i);

            // Required Format (LIST VIEW)
            display.append("\n--- Message ").append(i + 1).append(" ---\n")
                    .append("MessageID: ").append(uniqueId).append("\n")
                    .append("Message Hash: ").append(hash).append("\n")
                    .append("Recipient: ").append(recipientFullName).append("\n") // Use combined name/number
                    .append("Message: ").append(messageContent).append("\n");
        }
        JOptionPane.showMessageDialog(null, display.toString(), "Sent Messages", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Prints all stored messages in the required format: MessageID (Temp), Message Hash (Temp), Recipient (Intended), Message.
     */
    public void printStoredMessages() {
        if (storedMessageContent.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages are currently stored.");
            return;
        }

        StringBuilder display = new StringBuilder("*** STORED MESSAGES ***\n");
        String recipientNumber = (sentRecipientPhone.isEmpty()) ? "Unknown" : sentRecipientPhone.get(sentRecipientPhone.size() -1); // Use the last intended recipient number (heuristic)
        String recipientFullName = (currentRecipientName == null || currentRecipientName.isEmpty())
                                     ? recipientNumber
                                     : (currentRecipientName + " (" + recipientNumber + ")");

        for (int i = 0; i < storedMessageContent.size(); i++) {
            String uniqueId = storedMessageID.get(i);
            String hash = storedHashID.get(i);
            String messageContent = storedMessageContent.get(i);

            // Required Format (LIST VIEW - using stored temporary data)
            display.append("\n--- Stored Message ").append(i + 1).append(" ---\n")
                    .append("MessageID (Temp): ").append(uniqueId).append("\n")
                    .append("Message Hash (Temp): ").append(hash).append("\n")
                    .append("Recipient (Intended): ").append(recipientFullName).append("\n") // Use combined name/number
                    .append("Message: ").append(messageContent).append("\n");
        }
        JOptionPane.showMessageDialog(null, display.toString(), "Stored Messages", JOptionPane.INFORMATION_MESSAGE);
    }

    public void printDisregardedMessages() {
        if (disregardMessage.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages were disregarded.");
            return;
        }
        StringBuilder display = new StringBuilder("*** DISREGARDED MESSAGES ***\n");
        for (int i = 0; i < disregardMessage.size(); i++) {
            display.append("\nMessage ").append(i + 1).append(":\n")
                    .append("Content: ").append(disregardMessage.get(i)).append("\n");
        }
        JOptionPane.showMessageDialog(null, display.toString(), "Disregarded Messages", JOptionPane.INFORMATION_MESSAGE);
    }

    public String createMessageId() {
        String id;
        do {
            int firstNum = 1 + random.nextInt(9);
            int remainingNum = random.nextInt(1_000_000_000);
            id = firstNum + String.format("%09d", remainingNum);
        } while (uniqueIDTracker.contains(id)); // Check against the global unique ID tracker
        uniqueIDTracker.add(id); // Add the new ID to the tracker
        return id;
    }

    
    public String createMessageHash(String messageId, int messageNum, String message) {
        String[] words = message.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : (words.length == 1 ? firstWord : "");

        // Format: ID-prefix + ":" + messageNum + "-" + FIRSTWORD.toUpperCase() + LASTWORD.toUpperCase()
        String hash = messageId.substring(0, 2) + ":" + messageNum + "-" +
                      firstWord.toUpperCase() + lastWord.toUpperCase();

        return hash;
    }


    //PART 3 - Message Management

    public void messageManagement() {

        String [] options = {"Search ID","Delete by hash","Show all sent messages","Display longest message","Search for messages using the recipient number","Read JSON data"};
        int action = JOptionPane.showOptionDialog(null,
                "Message management",
                "Management",
                JOptionPane.DEFAULT_OPTION ,
                JOptionPane.INFORMATION_MESSAGE ,
                null,
                options,
                options[0]);

        switch (action){
            case 0 -> searchbyID();
            case 1 -> deletebyHash();
            case 2 -> printSentMessages();
            case 3 -> displaylongestmessage() ; 
            case 4 -> searchMessagesbyrecipient (); 
            case 5 -> readJsonFile("messages.json");
            // Default case (closing dialog) returns to sign-in loop
        }
    }

    public void searchbyID() {

        if (sentMessageID.isEmpty()){
            JOptionPane.showMessageDialog(null,"No unique ID to search in sent messages.");
            return;
        }

        String searchID = JOptionPane.showInputDialog(null,"Enter unique ID you want to search (10 digits)");
        if (searchID == null){
            return;
        }

        // Correctly search for the index of the ID in the parallel ArrayList
        int index = sentMessageID.indexOf(searchID);

        if (index == -1){
            JOptionPane.showMessageDialog(null,"Message ID not found.");
        } else {
            // Correctly retrieve all details using the found index
            String messageID = sentMessageID.get(index);
            String hash = hashID.get(index);
            String recipientNum = sentRecipientPhone.get(index); // Use the correct parallel list
            String messageContent = sentMessage.get(index);

            // Reconstruct the recipient full name for display
            String recipientFullName = (currentRecipientName == null || currentRecipientName.isEmpty())
                                       ? recipientNum
                                       : (currentRecipientName + " (" + recipientNum + ")");

            StringBuilder display = new StringBuilder("\n*** MESSAGE DETAILS FOUND ***\n");

            display.append("\n--- Found Message ---\n")
                   .append("MessageID: ").append(messageID).append("\n")
                   .append("Message Hash: ").append(hash).append("\n")
                   .append("Recipient: ").append(recipientFullName).append("\n")
                   .append("Message: ").append(messageContent).append("\n");

            JOptionPane.showMessageDialog(null,display.toString());
        }
    }


    public void deletebyHash() {

        if (hashID.isEmpty()){
            JOptionPane.showMessageDialog(null,"No Hash ID to use to delete the message details");
            return;
        }
        String hash = JOptionPane.showInputDialog("Enter Hash ID to delete message");
        if (hash == null){
            return;
        }

        int index = hashID.indexOf(hash);
        if (index != -1){
            // Get the message content and ID before removal for tracking
            String messageContent = sentMessage.get(index);
            String messageID = sentMessageID.get(index);

            // Correctly remove from all parallel lists using the index
            disregardMessage.add(messageContent); // Add to disregard list
            sentMessage.remove(index);
            sentRecipientPhone.remove(index); // Remove recipient number
            sentMessageID.remove(index); // Remove ID from ArrayList
            hashID.remove(index); // Remove Hash

            // Remove the ID from the global tracker so it can be potentially reused
            uniqueIDTracker.remove(messageID); 

            JOptionPane.showMessageDialog( null,
                 "Message successfully deleted and moved to Disregarded Messages!",
                 "Delete Success",
                 JOptionPane.DEFAULT_OPTION);

        }else{
            JOptionPane.showMessageDialog(null,
            "No message exists with this Hash!",null,JOptionPane.ERROR_MESSAGE);
        }

    }

    public void displaylongestmessage() {
        if (sentMessage.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages to analyze for longest message.");
            return;
        }

        int maxLength = -1;
        int longestIndex = -1;

        // 1. Find the index of the longest message
        for (int i = 0; i < sentMessage.size(); i++) {
            String message = sentMessage.get(i);
            if (message.length() > maxLength) {
                maxLength = message.length();
                longestIndex = i;
            }
        }

        // 2. Retrieve details and display (using the found index)
        if (longestIndex != -1) {
            String messageID = sentMessageID.get(longestIndex);
            String hash = hashID.get(longestIndex);
            String recipientNum = sentRecipientPhone.get(longestIndex);
            String messageContent = sentMessage.get(longestIndex);

            String recipientFullName = (currentRecipientName == null || currentRecipientName.isEmpty())
                                       ? recipientNum
                                       : (currentRecipientName + " (" + recipientNum + ")");

            String displayMessage = String.format(
                "*** LONGEST SENT MESSAGE (%d CHARACTERS) ***\n\n" +
                "MessageID: %s\n" +
                "Message Hash: %s\n" +
                "Recipient: %s\n" +
                "Message: %s",
                maxLength, messageID, hash, recipientFullName, messageContent
            );

            JOptionPane.showMessageDialog(null, displayMessage, "Longest Sent Message", JOptionPane.INFORMATION_MESSAGE);
        }
    } 
     
   public void searchMessagesbyrecipient () { 
    
   String searchNumber;
        do {
            searchNumber = JOptionPane.showInputDialog("Enter the recipient's phone number to search: (+27XXXXXXXXX or 0XXXXXXXXX)");
            if (searchNumber == null)
                return;
        } while (!Validation.checkCellPhoneNumber(searchNumber));

        StringBuilder results = new StringBuilder("*** SEARCH RESULTS FOR RECIPIENT: " + searchNumber + " ***\n\n");
        int foundCount = 0;

        // --- 1. Search Sent Messages ---
        results.append("--- SENT MESSAGES ---\n");
        for (int i = 0; i < sentMessage.size(); i++) {
            if (sentRecipientPhone.get(i).equals(searchNumber)) {
                String id = sentMessageID.get(i);
                String hash = hashID.get(i);
                String messageContent = sentMessage.get(i);
                
                results.append(String.format("ID: %s | Hash: %s\n", id, hash));
                results.append(String.format("Content: %s\n\n", messageContent));
                foundCount++;
            }
        }
        if (foundCount == 0) {
            results.append("No sent messages found for this recipient.\n\n");
        } else {
            results.append(String.format("Total sent messages found: %d\n\n", foundCount));
        }

        // --- 2. Search Stored Messages ---
        foundCount = 0; // Reset counter for stored messages
        results.append("--- STORED MESSAGES (Intended Recipient) ---\n");
        
        
        String lastRecipient = sentRecipientPhone.isEmpty() ? null : sentRecipientPhone.get(sentRecipientPhone.size() - 1);
        
        if (lastRecipient != null && lastRecipient.equals(searchNumber) && !storedMessageContent.isEmpty()) {
             for (int i = 0; i < storedMessageContent.size(); i++) {
                String id = storedMessageID.get(i);
                String hash = storedHashID.get(i);
                String messageContent = storedMessageContent.get(i);
                
                results.append(String.format("ID (Temp): %s | Hash (Temp): %s\n", id, hash));
                results.append(String.format("Content: %s\n\n", messageContent));
                foundCount++;
            }
        }
        
        if (foundCount == 0) {
             results.append("No stored messages found intended for this recipient (based on last session).\n\n");
        } else {
            results.append(String.format("Total stored messages found: %d\n\n", foundCount));
        }


        if (sentMessage.isEmpty() && storedMessageContent.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages have been sent or stored yet.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, results.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    } 
   
     public void readJsonFile(String fileName) {
        // 1. Clear previous data
        externalData.clear();
        
        try {
            // 2. Read all lines from the file path
            java.util.List<String> lines = Files.readAllLines(Paths.get(fileName));
            
            // 3. Process and store lines
            for (String line : lines) {
                String trimmedLine = line.trim();
                
                // Skip JSON structural elements and empty lines for simplified array storage
                if (!trimmedLine.startsWith("{") && !trimmedLine.startsWith("}") && !trimmedLine.isEmpty()) {
                    // Clean up common JSON punctuation (commas and quotes) for simpler viewing
                    externalData.add(trimmedLine.replace(",", "").replace("\"", ""));
                }
            }

            JOptionPane.showMessageDialog(null, 
                String.format("Successfully read data from **%s**. Total data entries stored: %d.", 
                fileName, externalData.size()), 
                "JSON Read Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            // Handle file not found or other I/O errors
            JOptionPane.showMessageDialog(null, 
                "Error reading file **" + fileName + "**. Please ensure the file exists in the project root.\nError: " + e.getMessage(), 
                "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
   
   
   
    
    
     
