package com.example.crudapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")  // Ensure table name matches schema.sql
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"roles", "orders"}) // Prevents infinite loops
@EqualsAndHashCode(exclude = {"roles", "orders"}) // Prevents stack overflow in collections
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;  // ✅ Added missing field
    private String lastName;   // ✅ Added missing field
    private String address;    // ✅ Added missing field
    private String phone;      // ✅ Added missing field

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();  // Ensures roles is never null

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Prevents recursion
    private List<Order> orders = new ArrayList<>();

}
