package com.example.crudapp.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String message;
    private String type;
    private Long orderId;
}