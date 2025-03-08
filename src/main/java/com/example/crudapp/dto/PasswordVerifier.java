package com.example.crudapp.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordVerifier {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "admin123";
        String hashedPassword = "$2a$10$KM2kMyJPMmGjF1UjmLzb6eX4KIdLGDkGPVauLbsW3ny6OOFMcJiVu";

        boolean isMatch = encoder.matches(rawPassword, hashedPassword);
        System.out.println("Password Match: " + isMatch);
    }
}