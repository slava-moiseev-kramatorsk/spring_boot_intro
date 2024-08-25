package com.example.demo.dto.category;

public record CreateCategoryDto(
        Long id,
        String name,
        String description
) {
}
