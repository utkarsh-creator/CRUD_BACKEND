package com.example.crudapp.service.impl;

import com.example.crudapp.dto.OrderDTO;
import com.example.crudapp.exception.ResourceNotFoundException;
import com.example.crudapp.model.Order;
import com.example.crudapp.model.OrderItem;
import com.example.crudapp.model.Product;
import com.example.crudapp.model.User;
import com.example.crudapp.repository.OrderRepository;
import com.example.crudapp.repository.ProductRepository;
import com.example.crudapp.repository.UserRepository;
import com.example.crudapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

                    return createOrderItem(order, product, item.getQuantity());
                })
                .collect(Collectors.toList());

        order.setItems(orderItems);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
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

        orderRepository.delete(order);
    }

    @Override
    public boolean isOrderOwner(Long orderId, String username) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        return orderOptional.map(order -> order.getUser().getUsername().equals(username)).orElse(false);
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
}
