package com.example.demo.repository.order;

import com.example.demo.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book", "user"})
    List<Order> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {"orderItems", "orderItems.book", "user"})
    Optional<Order> findByIdAndUserId(Long userId, Long orderId);
}
