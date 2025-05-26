package com.example.demo.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryDto;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Create a new Category")
    void createNewCategory_WithValidData_Ok() {
        CreateCategoryDto createCategoryDto = generateCategory();

        Category category = new Category(1L);
        category.setId(1L);
        category.setName("Fiction");

        CategoryDto expected = new CategoryDto();
        expected.setName(createCategoryDto.name());
        expected.setDescription(createCategoryDto.description());
        expected.setId(createCategoryDto.id());

        when(categoryMapper.toModel(createCategoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.save(createCategoryDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find all categories test")
    void findAllCategories_ReturnListOfCategoryDto() {
        Category category = new Category(1L);
        category.setId(1L);
        category.setName("Action");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        List<Category> categoryList = List.of(category);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(categoryList, pageable, categoryList.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> actual = categoryService.findAll(pageable);

        assertEquals(categoryDto, actual.get(0));
    }

    @Test
    @DisplayName("Test to delete category")
    void deleteCategory_withValidId_Ok() {
        Long id = 3L;
        categoryService.deleteByID(id);

        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Find category by id")
    void findCategoryById_WithValidId_Ok() {
        Long id = 5L;
        Category category = new Category(id);
        category.setName("Doc");
        category.setDescription("Interesting");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.getById(id);

        assertEquals(actual, categoryDto);
    }

    private CreateCategoryDto generateCategory() {
        return new CreateCategoryDto(
                1L,
                "Test",
                "TestCategory"
        );
    }
}
