package com.example.crudapp.controller;

import com.example.crudapp.dto.AuthRequest;
import com.example.crudapp.dto.AuthResponse;
import com.example.crudapp.dto.RegisterRequest;
import com.example.crudapp.model.User;
import com.example.crudapp.security.JwtUtil;
import com.example.crudapp.service.CustomUserDetailsService;
import com.example.crudapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil; // Changed from JwtUtil to JwtService
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Convert RegisterRequest DTO to User entity
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());

            // Call the UserService to save the user
            userService.registerUser(request);

            // Generate token for the newly registered user
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(userDetails);

            // Return token in a structured response
            AuthResponse authResponse = new AuthResponse(token, request.getUsername());
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println("Authenticating user: " + authRequest.getUsername());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            System.out.println("Authentication successful for user: " + authRequest.getUsername());

            String token = jwtUtil.generateToken(userDetails); // Changed from jwtUtil to jwtService

            // Return token in a structured response
            AuthResponse authResponse = new AuthResponse(token, authRequest.getUsername());
            return ResponseEntity.ok(authResponse);

        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // Simple endpoint to validate token/authentication
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken() {
        return ResponseEntity.ok("Token is valid");
    }
}