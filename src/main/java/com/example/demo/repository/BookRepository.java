package com.example.demo.repository;

import java.util.List;
import com.example.demo.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
