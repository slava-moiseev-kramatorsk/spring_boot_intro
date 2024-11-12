package com.example.demo.controller;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import java.math.BigDecimal;
import java.util.Set;

public class TestUtil {
    static CreateBookRequestDto createSampleCreateBookRequestDto() {
        return new CreateBookRequestDto()
                .setId(1L)
                .setAuthor("Slava")
                .setTitle("Testing")
                .setIsbn("9834343")
                .setPrice(BigDecimal.valueOf(5.5))
                .setDescription("This is for test")
                .setCoverImage("/url")
                .setCategoriesIds(Set.of(1L));
    }

    static BookDto createExpectedBookDto(CreateBookRequestDto requestDto) {
        return new BookDto()
                .setId(requestDto.getId())
                .setAuthor(requestDto.getAuthor())
                .setTitle(requestDto.getTitle())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoryIds(requestDto.getCategoriesIds());
    }
}
