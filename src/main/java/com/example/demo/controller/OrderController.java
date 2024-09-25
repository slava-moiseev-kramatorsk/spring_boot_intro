package com.example.demo.controller;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.OrderUpdateDto;
import com.example.demo.dto.orderitem.OrderItemDto;
import com.example.demo.model.User;
import com.example.demo.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management",
        description = "Endpoints for management of orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create order",
            description = "Create order for current user")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public OrderResponseDto create(@RequestBody @Valid OrderRequestDto requestDto,
                                   Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(user, requestDto);
    }

    @Operation(summary = "Create order",
            description = "Create order for current user")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/{id}")
    public OrderResponseDto updateOrder(@RequestBody @Valid OrderUpdateDto updateDto,
                                        @PathVariable Long orderId) {
        return orderService.updateOrderStatus(orderId, updateDto);
    }

    @Operation(summary = "Find order",
            description = "Find order for current user")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<OrderResponseDto> findByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findByUser(user);
    }

    @Operation(summary = "Get items",
            description = "Get item from current order")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items/{itemId}")
    public OrderItemDto getOrderItem(@PathVariable @Positive Long orderId,
                                     @PathVariable @Positive Long itemId) {
        return orderService.getOrderItemFromOrderById(orderId, itemId);
    }
}
