package com.example.crudapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")  // Ensure table name matches schema.sql
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "roles") // Prevents infinite loops
@EqualsAndHashCode(exclude = "roles") // Prevents stack overflow in collections
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false) // ✅ Added email field
    private String email;

    @Column(nullable = false) // ✅ Ensures password is not null
    private String password;

    @ManyToMany(fetch = FetchType.EAGER) // ✅ Use LAZY to improve performance
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
