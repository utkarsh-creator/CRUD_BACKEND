package com.example.crudapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}

//
//AuthRequest
//
//Purpose: Used for login requests
//Fields:
//
//username: Required field for authentication
//password: Required field for authentication
//
//
//Validation: Uses @NotBlank to ensure these fields are provided