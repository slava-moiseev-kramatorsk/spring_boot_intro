package com.example.demo.service.order;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.OrderUpdateDto;
import com.example.demo.dto.orderitem.OrderItemDto;
import com.example.demo.model.User;
import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(User user, OrderRequestDto requestDto);

    List<OrderResponseDto> findByUser(User user);

    OrderResponseDto updateOrderStatus(Long orderId, OrderUpdateDto updateDto);

    OrderItemDto getOrderItemFromOrderById(Long orderId, Long orderItemsId);
}
