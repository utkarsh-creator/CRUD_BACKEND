package com.example.crudapp.repository;

import com.example.crudapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); // ✅ Added method to search by email
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // ✅ Case-insensitive search
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
