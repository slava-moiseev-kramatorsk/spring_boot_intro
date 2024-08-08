package com.example.demo.dto;

import com.example.demo.validation.Author;
import com.example.demo.validation.Description;
import com.example.demo.validation.Isbn;
import com.example.demo.validation.Title;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

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
}
