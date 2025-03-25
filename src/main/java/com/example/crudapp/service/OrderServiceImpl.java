//package com.example.crudapp.service.impl;
//
//import com.example.crudapp.dto.OrderDTO;
//import com.example.crudapp.exception.ResourceNotFoundException;
//import com.example.crudapp.model.Order;
//import com.example.crudapp.model.OrderItem;
//import com.example.crudapp.model.Product;
//import com.example.crudapp.model.User;
//import com.example.crudapp.repository.OrderRepository;
//import com.example.crudapp.repository.ProductRepository;
//import com.example.crudapp.repository.UserRepository;
//import com.example.crudapp.service.OrderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class OrderServiceImpl implements OrderService {
//
//    private final OrderRepository orderRepository;
//    private final UserRepository userRepository;
//    private final ProductRepository productRepository;
//
//    @Override
//    @Transactional
//    public Order createOrder(OrderDTO orderDTO) {
//        // Get current authenticated user
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        // Create new order
//        Order order = new Order();
//        order.setUser(user);
//        order.setOrderDate(LocalDateTime.now());
//        order.setStatus("NEW");
//        order.setTotalAmount(calculateTotalAmount(orderDTO));
//
//        // Set order items
//        List<OrderItem> orderItems = orderDTO.getItems().stream()
//                .map(item -> {
//                    Product product = productRepository.findById(item.getProductId())
//                            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.getProductId()));
//
//                    // Check if sufficient stock is available
//                    if (product.getStockQuantity() < item.getQuantity()) {
//                        throw new IllegalStateException("Insufficient stock for product: " + product.getName()
//                                + ". Available: " + product.getStockQuantity() + ", Requested: " + item.getQuantity());
//                    }
//
//                    // Update product stock
//                    product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
//                    productRepository.save(product);
//
//                    return createOrderItem(order, product, item.getQuantity());
//                })
//                .collect(Collectors.toList());
//
//        order.setItems(orderItems);
//        return orderRepository.save(order);
//    }
//
//    @Override
//    public Order getOrderById(Long id) {
//        return orderRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
//    }
//
//    @Override
//    public List<Order> getAllOrders() {
//        return orderRepository.findAll();
//    }
//
//    @Override
//    public List<Order> getAllOrders(int page, int limit) {
//        return orderRepository.findAll(PageRequest.of(page, limit)).getContent();
//    }
//
//    @Override
//    public List<Order> getOrdersByUserId(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
//        return orderRepository.findByUser(user);
//    }
//
//    @Override
//    @Transactional
//    public Order updateOrderStatus(Long id, String status) {
//        Order order = orderRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
//
//        // If status is changing to CANCELLED and previous status wasn't CANCELLED or DELIVERED,
//        // restore product stock quantities
//        if ("CANCELLED".equals(status) &&
//                !"CANCELLED".equals(order.getStatus()) &&
//                !"DELIVERED".equals(order.getStatus())) {
//
//            restoreStockQuantities(order);
//        }
//
//        order.setStatus(status);
//        return orderRepository.save(order);
//    }
//
//    @Override
//    @Transactional
//    public Order cancelOrder(Long id) {
//        Order order = orderRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
//
//        // Only allow cancellation if order is not already processed
//        if (!"DELIVERED".equals(order.getStatus()) && !"CANCELLED".equals(order.getStatus())) {
//            // Restore stock quantities
//            restoreStockQuantities(order);
//
//            order.setStatus("CANCELLED");
//            return orderRepository.save(order);
//        } else {
//            throw new IllegalStateException("Cannot cancel order in state: " + order.getStatus());
//        }
//    }
//
//    @Override
//    @Transactional
//    public void deleteOrder(Long id) {
//        Order order = orderRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
//
//        // Restore stock quantities if order is being deleted and hasn't been delivered
//        if (!"DELIVERED".equals(order.getStatus()) && !"CANCELLED".equals(order.getStatus())) {
//            restoreStockQuantities(order);
//        }
//
//        orderRepository.delete(order);
//    }
//
//    @Override
//    public boolean isOrderOwner(Long orderId, String username) {
//        Optional<Order> orderOptional = orderRepository.findById(orderId);
//        return orderOptional.map(order -> order.getUser().getUsername().equals(username)).orElse(false);
//    }
//
//    // Helper methods
//    private double calculateTotalAmount(OrderDTO orderDTO) {
//        return orderDTO.getItems().stream()
//                .mapToDouble(item -> {
//                    Product product = productRepository.findById(item.getProductId())
//                            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.getProductId()));
//                    return product.getPrice() * item.getQuantity();
//                })
//                .sum();
//    }
//
//    private OrderItem createOrderItem(Order order, Product product, int quantity) {
//        OrderItem item = new OrderItem();
//        item.setOrder(order);
//        item.setProduct(product);
//        item.setQuantity(quantity);
//        item.setPrice(product.getPrice());
//        return item;
//    }
//
//    /**
//     * Restores stock quantities for all items in an order
//     * Used when cancelling or deleting an order
//     */
//    private void restoreStockQuantities(Order order) {
//        for (OrderItem item : order.getItems()) {
//            Product product = item.getProduct();
//            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
//            productRepository.save(product);
//        }
//    }
//}
package com.example.crudapp.service;

import com.example.crudapp.dto.OrderDTO;
import com.example.crudapp.dto.NotificationDTO;
import com.example.crudapp.exception.ResourceNotFoundException;
import com.example.crudapp.model.*;
import com.example.crudapp.repository.NotificationRepository;
import com.example.crudapp.repository.OrderRepository;
import com.example.crudapp.repository.ProductRepository;
import com.example.crudapp.repository.UserRepository;
import com.example.crudapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        // Get current authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Create new order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setTotalAmount(calculateTotalAmount(orderDTO));

        // Set order items
        List<OrderItem> orderItems = orderDTO.getItems().stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.getProductId()));

                    // Update product stock (allowing negative values)
                    product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
                    productRepository.save(product);

                    return createOrderItem(order, product, item.getQuantity());
                })
                .collect(Collectors.toList());

        order.setItems(orderItems);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));

        // Get current authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if order belongs to the current user (unless user is admin)
        if (!order.getUser().getUsername().equals(username) &&
                !SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("You do not have permission to access this order");
        }

        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllOrders(int page, int limit) {
        return orderRepository.findAll(PageRequest.of(page, limit)).getContent();
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        return orderRepository.findByUser(user);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));

        // If status is changing to CANCELLED and previous status wasn't CANCELLED or DELIVERED,
        // restore product stock quantities
        if ("CANCELLED".equals(status) &&
                !"CANCELLED".equals(order.getStatus()) &&
                !"DELIVERED".equals(order.getStatus())) {

            restoreStockQuantities(order);
        }

        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));

        // Only allow cancellation if order is not already processed
        if (!"DELIVERED".equals(order.getStatus()) && !"CANCELLED".equals(order.getStatus())) {
            // Restore stock quantities
            restoreStockQuantities(order);

            order.setStatus("CANCELLED");
            return orderRepository.save(order);
        } else {
            throw new IllegalStateException("Cannot cancel order in state: " + order.getStatus());
        }
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));

        // Restore stock quantities if order is being deleted and hasn't been delivered
        if (!"DELIVERED".equals(order.getStatus()) && !"CANCELLED".equals(order.getStatus())) {
            restoreStockQuantities(order);
        }

        orderRepository.delete(order);
    }

    @Override
    @Transactional
    public void notifyUser(Long userId, NotificationDTO notificationDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(notificationDTO.getType());
        notification.setMessage(notificationDTO.getMessage());
        notification.setOrderId(notificationDTO.getOrderId());

        notificationRepository.save(notification);
    }

    @Override
    public boolean isOrderOwner(Long orderId, String username) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        return orderOptional.map(order -> order.getUser().getUsername().equals(username)).orElse(false);
    }
        @Override
    @Transactional
    public Order acceptOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));

        // Only allow acceptance if order is in NEW status
        if (!"NEW".equals(order.getStatus())) {
            throw new IllegalStateException("Can only accept orders in NEW status. Current status: " + order.getStatus());
        }

        // Update order status to ACCEPTED
        order.setStatus("ACCEPTED");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getCurrentUserOrders() {
        // Get current authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return orderRepository.findByUser(user);
    }

    // Helper methods
    private double calculateTotalAmount(OrderDTO orderDTO) {
        return orderDTO.getItems().stream()
                .mapToDouble(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.getProductId()));
                    return product.getPrice() * item.getQuantity();
                })
                .sum();
    }

    private OrderItem createOrderItem(Order order, Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());
        return item;
    }

    private void restoreStockQuantities(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
    }
}