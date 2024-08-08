package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookSearchParameters;
import com.example.demo.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters parameters);
}
