package com.example.crudapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long productId;  // ✅ Identifies which product
    private Integer quantity;  // ✅ Tracks how many units
}
//
//OrderItemDTO
//
//Purpose: Represents individual items within an order
//Fields:
//
//productId: ID of the product being ordered
//quantity: Number of units ordered
//
//
//Used by: OrderDTO to build the complete order structure