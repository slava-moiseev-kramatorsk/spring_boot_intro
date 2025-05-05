package com.example.demo.service.book;

import com.example.demo.model.Book;
import com.example.demo.model.Category;
import java.util.Set;

public class TestUtilService {
    static Book generateFirstBook() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Slava");
        book.setTitle("Random book");
        book.setCategories(Set.of(generateCategory()));
        return book;
    }

    static Book generateSecondBook() {
        Book book = new Book();
        book.setAuthor("Platon");
        book.setTitle("Another");
        book.setCategories(Set.of(generateCategory()));
        return book;
    }

    private static Category generateCategory() {
        Category category = new Category(1L);
        category.setName("TestCategory");
        return category;
    }
}
