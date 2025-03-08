//package com.example.crudapp.controller;
//
//import com.example.crudapp.dto.OrderDTO;
//import com.example.crudapp.model.Order;
//import com.example.crudapp.service.OrderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/orders")
//@RequiredArgsConstructor
//public class OrderController {
//
//    private final OrderService orderService;
//
//    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Both ADMIN and USER can create orders
//    public ResponseEntity<Order> placeOrder(@RequestBody OrderDTO orderDTO) {
//        return ResponseEntity.ok(orderService.createOrder(orderDTO));
//    }
//
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Both ADMIN and USER can view orders
//    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//        return ResponseEntity.ok(orderService.getOrderById(id));
//    }
//
//    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can view all orders
//    public ResponseEntity<List<Order>> getAllOrders() {
//        return ResponseEntity.ok(orderService.getAllOrders());
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can delete orders
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
//        orderService.deleteOrder(id);
//        return ResponseEntity.noContent().build();
//    }
//}
package com.example.crudapp.controller;

import com.example.crudapp.dto.OrderDTO;
import com.example.crudapp.model.Order;
import com.example.crudapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Order> placeOrder(
            @RequestBody OrderDTO orderDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // ✅ ADDED: Pagination support for order retrieval
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(orderService.getAllOrders(page, limit));
    }

    // ✅ ADDED: Fetch orders by user ID (Admin & Order Owner)
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.username == @userService.getUserById(#userId).get().username")
    public ResponseEntity<List<Order>> getOrdersByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    // ✅ ADDED: Update order status (Admin only)
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusData) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, statusData.get("status")));
    }

    // ✅ ADDED: Cancel order (Admin & Order Owner)
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @orderService.isOrderOwner(#id, #userDetails.username)")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
