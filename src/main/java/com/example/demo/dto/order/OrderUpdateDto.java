package com.example.demo.dto.order;

import com.example.demo.model.Order;
import lombok.Data;

@Data
public class OrderUpdateDto {
    private Order.Status status;
}
