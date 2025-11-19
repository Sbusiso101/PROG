/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qcapplic.ation;

/**
 *
 * @author Sbusiso
 */ 
 
import javax.swing.JOptionPane; 

class Validation {
    
    
     /**
     * Validates that the username contains an underscore and is no more than five characters in length.
     * @param username The string to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean checkUsername(String username) {
        // Condition: must contain '_' AND length must be <= 5
        if (username != null && username.contains("_") && username.length() <= 5) {
            JOptionPane.showMessageDialog(null, "Username successfully captured");
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                    "Username not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
            return false;
        }
    }

    /**
     * Validates that the password is at least 8 characters long and contains:
     * one digit, one uppercase letter, one lowercase letter, and one special character (@#$%^&+=).
     * @param password The string to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean checkPassword(String password) {
        // Regex: (?=.*[0-9]) one digit; (?=.*[A-Z]) one uppercase; (?=.*[a-z]) one lowercase; (?=.*[@#$%^&+=]) one special; .{8,} at least 8 total.
        if (password != null && password.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=]).{8,}$")) {
            JOptionPane.showMessageDialog(null, "Password successfully captured");
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                    "Password not correctly formatted. Must be at least 8 characters and contain a capital letter, a lowercase letter, a number, and a special character.");
            return false;
        }
    }

    /**
     * Validates that the cellphone number is in the format +27XXXXXXXXX or 0XXXXXXXXX.
     * Assumes a standard South African format where the second digit (after 0 or +27) is 6, 7, or 8.
     * @param cellphone The string to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean checkCellPhoneNumber(String cellphone) {
        // Regex: (^(\\+27|0)) starts with +27 or 0; ([6-8][0-9]{8}$) followed by 6, 7, or 8 and 8 more digits. (Total 10 digits after 0, or 11 after +27)
        if (cellphone != null && cellphone.matches("^(\\+27|0)[6-8][0-9]{8}$")) {
            JOptionPane.showMessageDialog(null, "Cellphone number successfully added");
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                    "Cellphone number incorrectly formatted. Please use +27XXXXXXXXX or 0XXXXXXXXX.");
            return false;
        }
    }
}


