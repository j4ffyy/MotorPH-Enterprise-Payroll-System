/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ViewModel;

/**
 *
 * @author dashcodes
 */

import javax.swing.text.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RestrictedInput extends PlainDocument {
    boolean allowLetters;
    boolean allowSpaces;
    boolean allowHyphens;
    boolean allowApostrophes;
    boolean allowNumbers;
    boolean allowUpperCase;
    boolean allowAtSign;
    boolean allowDot;
    //boolean allowUnderscore; // added for future expansion of uses
    int maxLength;
    
    // Set of invalid words/phrases that should be rejected
    private static final Set<String> INVALID_WORDS = new HashSet<>(Arrays.asList(
        "InvalidInput", "invalid", "test", "error", "fail"
    ));

    // Constructor with comprehensive input restrictions
    public RestrictedInput() {
        this(true, true, false, false, false, true, false, true, 50); // Default: letters and spaces, max 50 chars
    }

    public RestrictedInput(boolean allowLetters, boolean allowSpaces,
                           boolean allowHyphens, boolean allowApostrophes,
                           boolean allowNumbers,
                           boolean allowUppercase,
                           boolean allowAtSign,
                           boolean allowDot,
                           int maxLength) {
        this.allowLetters = allowLetters;
        this.allowSpaces = allowSpaces;
        this.allowHyphens = allowHyphens;
        this.allowApostrophes = allowApostrophes;
        this.allowNumbers = allowNumbers;
        this.allowUpperCase = allowUppercase;
        this.allowAtSign = allowAtSign;
        this.allowDot = allowDot;
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }

        // Check for invalid words first
        if (containsInvalidWord(str)) {
            return; // Reject the entire input if it contains invalid words
        }

        // Create a StringBuilder to filter the input
        StringBuilder filteredStr = new StringBuilder();

        // Iterate through each character in the input string
        for (char c : str.toCharArray()) {
            if (isValidCharacter(c)) {
                filteredStr.append(c);
            } else {
                // Replace invalid characters with space (avoid double spaces)
                if (filteredStr.length() > 0 && filteredStr.charAt(filteredStr.length() - 1) != ' ') {
                    filteredStr.append(' ');
                }
            }
        }

        // Check total length after filtering valid characters
        int currentLength = getLength();
        if (currentLength + filteredStr.length() > maxLength) {
            // Limit the length to maxLength
            filteredStr.setLength(maxLength - currentLength);
        }

        // If there are valid characters after filtering, insert them
        if (filteredStr.length() > 0) {
            super.insertString(offs, filteredStr.toString(), a);
        }
    }

    @Override
    public void replace(int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
        if (str == null) {
            super.replace(offset, length, str, attrs);
            return;
        }
        
        // Apply uppercase restriction first if specified
        if (!allowUpperCase) {
            str = str.toLowerCase();
        }

        // Check for invalid words first
        if (containsInvalidWord(str)) {
            return; // Reject the replacement if it contains invalid words
        }

        // Create a StringBuilder to filter the input
        StringBuilder filteredStr = new StringBuilder();

        for (char c : str.toCharArray()) {
            if (isValidCharacter(c)) {
                filteredStr.append(c);
            } else {
                // Replace invalid characters with space (avoid double spaces)
                if (filteredStr.length() > 0 && filteredStr.charAt(filteredStr.length() - 1) != ' ') {
                    filteredStr.append(' ');
                }
            }
        }

        // Only perform the replacement if there are valid characters
        if (filteredStr.length() > 0) {
            // Check total length after replacement
            int currentLength = getLength();
            if (currentLength - length + filteredStr.length() <= maxLength) {
                super.replace(offset, length, filteredStr.toString(), attrs);
            } else {
             // If the replacement string would exceed max length, truncate it
            String truncatedStr = filteredStr.substring(0, Math.max(0, maxLength - (currentLength - length)));
            if (!truncatedStr.isEmpty()) {
                super.replace(offset, length, truncatedStr, attrs);
                }
            }
        }
    }

    // Helper method to check if input contains invalid words
    private boolean containsInvalidWord(String input) {
        if (input == null) return false;
        
        String lowerInput = input.toLowerCase().trim();
        
        // Check if the entire input is an invalid word
        if (INVALID_WORDS.contains(lowerInput)) {
            return true;
        }
        
        // Check if input contains any invalid words
        for (String invalidWord : INVALID_WORDS) {
            if (lowerInput.contains(invalidWord.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }

    // Helper method to validate characters based on input restrictions
    private boolean isValidCharacter(char c) {
        return (allowLetters && Character.isLetter(c)) ||
               (allowSpaces && c == ' ') ||
               (allowHyphens && c == '-') ||
               (allowApostrophes && c == '\'') ||
               (allowNumbers && Character.isDigit(c)) ||
               (allowAtSign && c == '@') ||
               (allowDot && c == '.');
    }

    // Static method to create a numbers-only document
    public static RestrictedInput numbersOnly(int maxLength) {
        return new RestrictedInput(
            false,   // No letters
            false,   // No spaces
            false,   // No hyphens
            false,   // No apostrophes
            true,    // Allow numbers
            true,   // Allow upper case
            false,  // Allow @ sign
            false,  // Allow dot sign    
            maxLength
        );
    }
    
    // Static method to create a document that doesn't allow upper case
    public static RestrictedInput allowLowerCaseOnly(int maxLength) {
        return new RestrictedInput(
            true,    // Allow letters
            false,   // Allow spaces 
            true,    // Allow hyphens 
            true,    // Allow apostrophes (adjust if needed)
            true,    // Allow numbers (adjust if needed)
            false,   // DO NOT allow upper case
            true,   // Allow @ sign
            true,  // Allow dot sign 
            maxLength
        );
    }

    public void setAllowLetters(boolean allowLetters) {
        this.allowLetters = allowLetters;
    }

    public void setAllowSpaces(boolean allowSpaces) {
        this.allowSpaces = allowSpaces;
    }

    public void setAllowHyphens(boolean allowHyphens) {
        this.allowHyphens = allowHyphens;
    }

    public void setAllowApostrophes(boolean allowApostrophes) {
        this.allowApostrophes = allowApostrophes;
    }

    public void setAllowNumbers(boolean allowNumbers) {
        this.allowNumbers = allowNumbers;        
    }
    
    public void setAllowUpperCase(boolean allowUpperCase){
        this.allowUpperCase = allowUpperCase;
    }
    
    public void setAllowAtSign (boolean allowAtSign) {
        this.allowAtSign = allowAtSign;
    }
    
    public void setAllowDot (boolean allowDot) {
        this.allowDot = allowDot;
    }            

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}