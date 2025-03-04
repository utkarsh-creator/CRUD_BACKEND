package com.example.crudapp.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {

    public static void main(String[] args) {
        // Create a BCryptPasswordEncoder instance
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Password to hash
        String rawPassword = "test123";

        // Hash the password
        String hashedPassword = encoder.encode(rawPassword);

        // Print the hashed password
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Hashed Password: " + hashedPassword);

        // Verify the password
        boolean isMatch = encoder.matches(rawPassword, hashedPassword);
        System.out.println("Password Match: " + isMatch);
    }
}