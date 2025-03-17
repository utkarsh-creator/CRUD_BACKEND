package com.example.crudapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
}
//
//AuthResponse
//
//Purpose: Returns authentication results
//Fields:
//
//token: The JWT token for the authenticated user
//username: The authenticated user's username
//
//
//Used by: AuthController to return login/register results