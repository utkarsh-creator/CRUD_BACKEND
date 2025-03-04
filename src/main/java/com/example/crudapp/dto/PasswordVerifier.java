package com.example.crudapp.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordVerifier {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "testuser123";
        String hashedPassword = "$2a$10$newHashedPassword";

        boolean isMatch = encoder.matches(rawPassword, hashedPassword);
        System.out.println("Password Match: " + isMatch);
    }
}