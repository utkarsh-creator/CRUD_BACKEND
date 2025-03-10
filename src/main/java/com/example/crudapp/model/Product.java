package com.example.crudapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @JsonIgnore // Prevent recursion
    private List<Order> orders = new ArrayList<>();

}
