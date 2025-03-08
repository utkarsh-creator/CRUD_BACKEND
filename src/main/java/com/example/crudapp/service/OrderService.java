package com.example.crudapp.service;

import com.example.crudapp.dto.OrderDTO;
import com.example.crudapp.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderDTO orderDTO);

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    List<Order> getAllOrders(int page, int limit);

    List<Order> getOrdersByUserId(Long userId);

    Order updateOrderStatus(Long id, String status);

    Order cancelOrder(Long id);

    void deleteOrder(Long id);

    boolean isOrderOwner(Long orderId, String username);
}
