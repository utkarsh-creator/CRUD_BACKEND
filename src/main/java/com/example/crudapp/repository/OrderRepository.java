package com.example.crudapp.repository;

import com.example.crudapp.model.Order;
import com.example.crudapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByUser(User user); // ✅ Fetch orders by user
}
