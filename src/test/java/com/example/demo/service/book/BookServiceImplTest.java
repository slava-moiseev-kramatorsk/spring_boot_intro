package com.example.demo.service.book;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.category.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
public class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Add one book to DB")
    public void saveBook_WithValidData_Ok() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("Slava");
        requestDto.setTitle("First_book_test");
        requestDto.setCategoriesIds(Collections.singleton(1L));

        Book book = generateFirstBook();

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setAuthor(book.getAuthor());
        expected.setTitle(book.getTitle());
        expected.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toUnmodifiableSet()));

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.save(requestDto);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find book by valid id")
    public void findById_WithValidBookId_Ok() {
        Book book = generateFirstBook();

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        String actual = bookService.findById(1L).getTitle();
        String expected = book.getTitle();

        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Get all books from DB")
    public void findAllBooks_shouldReturnAllBook() {
        Book firstBookbook = generateFirstBook();
        BookDto bookDto1 = new BookDto();
        bookDto1.setTitle(firstBookbook.getTitle());
        bookDto1.setAuthor(firstBookbook.getAuthor());
        bookDto1.setCategoryIds(firstBookbook.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        Book secondBook = generateSecondBook();
        BookDto bookDto2 = new BookDto();
        bookDto2.setAuthor(secondBook.getAuthor());
        bookDto2.setTitle(secondBook.getTitle());
        bookDto2.setCategoryIds(secondBook.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<>(List.of(firstBookbook, secondBook), pageable, 2);

        when(bookRepository.findAll(pageable)).thenReturn(page);
        when(bookMapper.toDto(firstBookbook)).thenReturn(bookDto1);
        when(bookMapper.toDto(secondBook)).thenReturn(bookDto2);

        final List<BookDto> actual = bookService.findAll(pageable);
        verify(bookRepository,times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(firstBookbook);
        verify(bookMapper, times(1)).toDto(secondBook);

        List<BookDto> expected = List.of(bookDto1, bookDto2);
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Get by wrong id, should throw exception")
    public void findById_WithWrongId_ShouldThrowException() {
        Long invalidId = -1L;

        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(invalidId)
        );
        String expected = "Book with this id " + invalidId + " not found";
        String actual = exception.getMessage();

        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Delete book by id from DB")
    public void deleteBook_WithValidId_Ok() {
        Long bookId = 1L;
        bookService.deleteById(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    private Category generateCategory() {
        Category category = new Category(1L);
        category.setName("TestCategory");
        return category;
    }

    private Book generateFirstBook() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Slava");
        book.setTitle("Random book");
        book.setCategories(Set.of(generateCategory()));
        return book;
    }

    private Book generateSecondBook() {
        Book book = new Book();
        book.setAuthor("Platon");
        book.setTitle("Another");
        book.setCategories(Set.of(generateCategory()));
        return book;
    }
}
