package com.example.demo.controller;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryDto;
import com.example.demo.service.book.BookService;
import com.example.demo.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(name = "Category Management",
        description = "Contains category entity management operations")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(summary = "Create new category",
              description = "Create new category and save to DB")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto create(@RequestBody @Valid CreateCategoryDto createCategoryDto) {
        return categoryService.save(createCategoryDto);
    }

    @Operation(summary = "Upload all categories",
            description = "Upload all categories from DB")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(summary = "Find category by id",
            description = "UFind category by id")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Update category",
            description = "Update category")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto update(
            @PathVariable @Positive Long id,
            @RequestBody @Valid CreateCategoryDto createCategoryDto
    ) {
        return categoryService.update(id, createCategoryDto);
    }

    @Operation(summary = "Delete category by Id")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive Long id) {
        categoryService.deleteByID(id);
    }

    @Operation(summary = "Find all books by category Id",
            description = "Upload all books by category Id")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/books")
    public List<BookDto> getBooksByCategoryId(
            @PathVariable @Positive Long id,
            @ParameterObject @PageableDefault Pageable pageable
    ) {
        return bookService.getBooksByCategoryId(id, pageable);
    }
}
