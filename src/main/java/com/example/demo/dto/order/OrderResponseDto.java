package com.example.demo.dto.order;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Set<OrderItem> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Order.Status status;
}
