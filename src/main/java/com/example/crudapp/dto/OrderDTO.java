package com.example.crudapp.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {
    private Long userId;
    private List<OrderItemDTO> orderItems;
    private double totalAmount;
}