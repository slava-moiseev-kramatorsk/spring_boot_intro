package com.example.demo.service;

import java.util.List;
import com.example.demo.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
