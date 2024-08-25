package com.example.demo.dto.book;

import com.example.demo.validation.book.Author;
import com.example.demo.validation.book.Description;
import com.example.demo.validation.book.Isbn;
import com.example.demo.validation.book.Title;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBookRequestDto {
    private Long id;
    @NotBlank
    @Title
    private String title;
    @NotBlank
    @Author
    private String author;
    @NotBlank
    @Isbn
    private String isbn;
    @Positive
    private BigDecimal price;
    @Description
    private String description;
    private String coverImage;
    private List<Long> categoriesIds;
}
