//package com.example.crudapp.controller;
//
//import com.example.crudapp.dto.RegisterRequest;
//import com.example.crudapp.dto.UserDTO;
//import com.example.crudapp.model.User;
//import com.example.crudapp.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterRequest request) { // ✅ Use RegisterRequest
//        User registeredUser = userService.registerUser(request); // ✅ Pass RegisterRequest
//        return ResponseEntity.ok(new UserDTO(registeredUser)); // ✅ Use correct variable
//    }
//
//    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
//        Optional<User> user = userService.getUserById(id);
//        return user.map(u -> ResponseEntity.ok(new UserDTO(u)))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
//}
package com.example.crudapp.controller;

import com.example.crudapp.dto.NotificationRequest;  // Add this import
import com.example.crudapp.service.NotificationService;  // Add this import
import com.example.crudapp.model.User;
import com.example.crudapp.dto.UserDTO;
import com.example.crudapp.dto.UserProfileDTO;
import com.example.crudapp.dto.PasswordChangeRequest;
import com.example.crudapp.dto.RegisterRequest;
import com.example.crudapp.service.UserService;
import com.example.crudapp.service.OrderService;
import com.example.crudapp.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final NotificationService notificationService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterRequest request) {
        User registeredUser = userService.registerUser(request);
        return ResponseEntity.ok(new UserDTO(registeredUser));
    }
    @GetMapping("/users/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/users/{userId}/notifications")
    public ResponseEntity<?> notifyUser(@PathVariable Long userId, @RequestBody NotificationRequest request) {
        try {
            notificationService.createNotification(userId, request.getMessage());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserByUsername(userDetails.getUsername())
                .map(user -> ResponseEntity.ok(new UserDTO(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<User> users = userService.getAllUsers(page, limit);
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.username == @userService.getUserById(#id).orElse(null)?.username")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(new UserDTO(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/profile")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.username == @userService.getUserById(#id).orElse(null)?.username")
    public ResponseEntity<UserProfileDTO> getUserProfile(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(new UserProfileDTO(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/profile")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.username == @userService.getUserById(#id).orElse(null)?.username")
    public ResponseEntity<UserProfileDTO> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UserProfileDTO profileData,
            @AuthenticationPrincipal UserDetails userDetails) {
        User updatedUser = userService.updateUserProfile(id, profileData);
        return ResponseEntity.ok(new UserProfileDTO(updatedUser));
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.username == @userService.getUserById(#id).orElse(null)?.username")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestBody PasswordChangeRequest passwordData,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.changeUserPassword(id, passwordData.getCurrentPassword(), passwordData.getNewPassword());
        return ResponseEntity.ok().body("Password changed successfully");
    }

    @GetMapping("/{id}/orders")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.username == @userService.getUserById(#id).orElse(null)?.username")
    public ResponseEntity<List<Order>> getUserOrders(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<Order> orders = orderService.getOrdersByUserId(id);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.username == @userService.getUserById(#id).orElse(null)?.username")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        User updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(new UserDTO(updatedUser));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> roleData) {
        User updatedUser = userService.updateUserRole(id, roleData.get("role"));
        return ResponseEntity.ok(new UserDTO(updatedUser));
    }
}

//UserController
//This controller handles user management at the /api/users endpoint:
//
//Register User (POST /api/users/register): Creates a new user
//
//Takes registration data in a RegisterRequest
//Returns user data in a UserDTO
//
//
//Get Current User (GET /api/users/me): Gets the logged-in user's information
//Get All Users (GET /api/users): Retrieves all users with pagination
//
//Admin-only endpoint
//Supports pagination with page and limit parameters
//
//
//Get User by ID (GET /api/users/{id}): Gets a specific user
//
//Available to admins and the user themselves
//
//
//Get User Profile (GET /api/users/{id}/profile): Gets a user's profile
//
//Available to admins and the user themselves
//
//
//Update User Profile (PUT /api/users/{id}/profile): Updates a user's profile
//
//Available to admins and the user themselves
//
//
//Change Password (PUT /api/users/{id}/password): Changes a user's password
//
//Available to admins and the user themselves
//
//
//Get User Orders (GET /api/users/{id}/orders): Gets a user's orders
//
//Available to admins and the user themselves
//
//
//Update User (PUT /api/users/{id}): Updates a user
//
//Available to admins and the user themselves
//
//
//Delete User (DELETE /api/users/{id}): Removes a user
//
//Admin-only endpoint
//
//
//Update User Role (PATCH /api/users/{id}/role): Changes a user's role
//
//Admin-only endpoint