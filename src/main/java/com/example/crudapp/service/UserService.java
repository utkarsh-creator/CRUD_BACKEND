package com.example.crudapp.service;

import com.example.crudapp.dto.RegisterRequest;
import com.example.crudapp.dto.UserProfileDTO;
import com.example.crudapp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(RegisterRequest registerRequest);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers(int page, int limit);

    User updateUserProfile(Long id, UserProfileDTO profileData);

    void changeUserPassword(Long id, String currentPassword, String newPassword);

    User updateUserRole(Long id, String role);

    void deleteUser(Long id);
}
