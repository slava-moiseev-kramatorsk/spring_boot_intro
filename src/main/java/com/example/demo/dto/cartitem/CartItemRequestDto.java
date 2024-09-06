package com.example.demo.dto.cartitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CartItemRequestDto(
        @Positive
        @NotBlank
        Long bookId,
        @Positive
        @NotBlank
        int quantity
) {
}
