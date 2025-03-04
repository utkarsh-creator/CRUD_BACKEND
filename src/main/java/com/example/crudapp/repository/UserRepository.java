package com.example.crudapp.repository;

import com.example.crudapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    // ✅ Add this method for searching users by username (case-insensitive)
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
