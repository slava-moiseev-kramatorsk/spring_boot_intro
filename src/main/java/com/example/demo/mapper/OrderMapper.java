package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.OrderUpdateDto;
import com.example.demo.model.Order;
import com.example.demo.model.ShoppingCart;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "orderDate", dateFormat = "yyyy-MM-dd HH:mm")
    OrderResponseDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "cart.user")
    @Mapping(target = "shippingAddress", source = "requestDto.shippingAddress")
    @Mapping(target = "status", ignore = true)
    Order toModel(ShoppingCart cart, OrderRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "shippingAddress", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateOrderStatus(OrderUpdateDto updateDto,
                           @MappingTarget Order order);

    default LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
