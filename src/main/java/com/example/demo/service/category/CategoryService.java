package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryDto createCategoryDto);

    CategoryDto update(Long id, CreateCategoryDto createCategoryDto);

    void deleteByID(Long id);
}
