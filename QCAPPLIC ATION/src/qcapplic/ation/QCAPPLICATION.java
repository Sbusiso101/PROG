/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package qcapplic.ation;

/**
 *
 * @author Sbusiso
 */
public class QCAPPLICATION {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    
    
        // Create an instance of the ChatApp to manage user registration and messaging logic
        MyChatApp app = new MyChatApp();

        // Main application loop
        while (true) {
            String[] options = {"Register", "Sign In", "Exit"};
            
            // Display the main menu options to the user using a JOptionPane dialog
            int choice = javax.swing.JOptionPane.showOptionDialog(
                    null, "Welcome! Choose an option:", "Chat Application",
                    javax.swing.JOptionPane.DEFAULT_OPTION,
                    javax.swing.JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]
            );
 
            
            // Process the user's choice
            switch (choice) {
                case 0 -> app.registerUser(); // Call the registration method in ChatApp
                case 1 -> app.signIn();       // Call the sign-in method in ChatApp
                // Handle Exit option (case 2) or clicking the close button (JOptionPane.CLOSED_OPTION)
                case 2, javax.swing.JOptionPane.CLOSED_OPTION -> System.exit(0); 
            }
        }
    }
}

    
    
    
    
    
