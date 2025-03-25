package com.example.crudapp.controller;

import com.example.crudapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import com.example.crudapp.dto.NotificationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserNotificationController {

    private final OrderService orderService;

    @PostMapping("/{userId}/notifications")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> notifyUser(
            @PathVariable Long userId,
            @RequestBody NotificationDTO notification) {

        orderService.notifyUser(userId, notification);
        return ResponseEntity.noContent().build();
    }
}