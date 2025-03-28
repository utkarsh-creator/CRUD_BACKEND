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
