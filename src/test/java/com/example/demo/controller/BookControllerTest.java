package com.example.demo.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.assertj.core.util.BigDecimalComparator;
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
@Transactional
class BookControllerTest {

    protected static MockMvc mockMvc;
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
                    new ClassPathResource("database/add-three-books.sql")
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
    @DisplayName("Create a new book")
    void createBook_ValidData_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setId(1L)
                .setAuthor("Slava")
                .setTitle("Testing")
                .setIsbn("9834343")
                .setPrice(BigDecimal.valueOf(5.5))
                .setDescription("This for test")
                .setCoverImage("/url")
                .setCategoriesIds(Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        BookDto expected = new BookDto()
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setId(requestDto.getId())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setTitle(requestDto.getTitle())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoryIds(requestDto.getCategoriesIds());

        MvcResult result = mockMvc.perform(
                post("/books")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Sql(scripts = "classpath:database/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/add-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all books from DB")
    void getAll_GivenAllBooks_ShouldReturnBookDto() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto()
                .setId(1L)
                .setTitle("Testing")
                .setAuthor("Robert")
                .setIsbn("866755")
                .setPrice(BigDecimal.valueOf(30)));
        expected.add(new BookDto()
                .setId(2L)
                .setTitle("Scream")
                .setAuthor("Slava")
                .setIsbn("435343534")
                .setPrice(BigDecimal.valueOf(28)));
        expected.add(new BookDto()
                .setId(3L)
                .setTitle("Orange")
                .setAuthor("Platon")
                .setIsbn("75675677")
                .setPrice(BigDecimal.valueOf(2)));

        MvcResult result = mockMvc.perform(
                get("/books")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actual = Arrays.stream(
                objectMapper.readValue(result.getResponse()
                        .getContentAsByteArray(), BookDto[].class))
                .toList();

        Assertions.assertEquals(expected.size(), actual.size());
        assertThat(actual)
                .usingRecursiveComparison()
                .withComparatorForType(new BigDecimalComparator(), BigDecimal.class)
                .ignoringFields("categoryIds")
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(scripts = "classpath:database/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/add-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get book by id from DB")
    void getBookById_WithValidId_ShouldReturnBookDto() throws Exception {
        Long id = 3L;
        BookDto expected = new BookDto()
                .setId(3L)
                .setTitle("Orange")
                .setAuthor("Platon")
                .setIsbn("75675677")
                .setPrice(BigDecimal.valueOf(2));

        MvcResult result = mockMvc.perform(get("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        EqualsBuilder.reflectionEquals(actual, expected);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    @Test
    @Sql(scripts = "classpath:database/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/add-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Delete book by id")
    void deleteBookById_WithValidId_Ok() throws Exception {
        Long id = 2L;
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto()
                .setId(1L)
                .setTitle("Testing")
                .setAuthor("Robert")
                .setIsbn("866755")
                .setPrice(BigDecimal.valueOf(30)));
        expected.add(new BookDto()
                .setId(3L)
                .setTitle("Orange")
                .setAuthor("Platon")
                .setIsbn("75675677")
                .setPrice(BigDecimal.valueOf(2)));

        mockMvc.perform(
                        delete("/books/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = Arrays.stream(
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        BookDto[].class)
        ).toList();
        Assertions.assertEquals(expected.size(), actual.size());
        EqualsBuilder.reflectionEquals(expected,actual);
    }
}
