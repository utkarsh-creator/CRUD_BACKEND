package com.example.crudapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description; // Added for product details

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private Integer stockQuantity; // Renamed for clarity

    @ManyToMany(mappedBy = "products")
    private Set<Order> orders = new HashSet<>();

}
