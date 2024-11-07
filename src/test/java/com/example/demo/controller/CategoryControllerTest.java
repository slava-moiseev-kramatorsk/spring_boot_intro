package com.example.demo.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/insert-into-categories.sql")
            );
        }
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-all-from-books.sql"));
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new category test")
    void createCategory_ShouldReturnCategoryDto() throws Exception {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto(
                1L,
                "Horror",
                "Scary book"
        );
        CategoryDto expected = new CategoryDto()
                .setId(createCategoryDto.id())
                .setName(createCategoryDto.name())
                .setDescription(createCategoryDto.description());

        String jsonRequest = objectMapper.writeValueAsString(createCategoryDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Sql(scripts = "classpath:database/insert-into-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get all categories from DB")
    void getAllCategories_ShouldReturnListOfCategoryDto() throws Exception {
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(new CategoryDto()
                .setId(1L)
                .setName("Dex")
                .setDescription("dex books"));
        expected.add(new CategoryDto()
                .setId(2L)
                .setName("Horror")
                .setDescription("Horror books"));
        expected.add(new CategoryDto()
                .setId(3L)
                .setName("Education")
                .setDescription("books for learning"));

        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<CategoryDto> actual = Arrays.stream(
                objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                        CategoryDto[].class)).toList();
        Assertions.assertEquals(expected.size(), actual.size());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Sql(scripts = "classpath:database/insert-into-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get categories by id")
    void getCategoryByID_WithValidId_ShouldReturnCategoryDto() throws Exception {
        Long id = 2L;
        CategoryDto expected = new CategoryDto()
                .setId(2L)
                .setName("Horror")
                .setDescription("Horror books");

        MvcResult result = mockMvc.perform(
                        get("/categories/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Sql(scripts = "classpath:database/insert-into-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete category by id")
    void deleteCategoryById_WithValidId_Ok() throws Exception {
        List<CategoryDto> expected = new ArrayList<>();
        Long id = 3L;
        expected.add(new CategoryDto()
                .setId(1L)
                .setName("Dex")
                .setDescription("dex books"));
        expected.add(new CategoryDto()
                .setId(2L)
                .setName("Horror")
                .setDescription("Horror books"));

        mockMvc.perform(
                        delete("/categories/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> actual = Arrays.stream(
                objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                        CategoryDto[].class)).toList();
        Assertions.assertEquals(expected.size(), actual.size());
        EqualsBuilder.reflectionEquals(expected,actual);
    }
}
