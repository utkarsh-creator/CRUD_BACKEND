package com.example.crudapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String username;  // ✅ Link order to a specific user
    private List<OrderItemDTO> items;  // ✅ Renamed for consistency
    private double totalAmount;  // ✅ Ensures total price tracking
}
