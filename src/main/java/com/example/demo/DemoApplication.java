package com.example.demo;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import com.example.demo.model.Book;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.demo.service.BookService;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {
    private final BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("1408");
            book.setAuthor("S.King");
            book.setPrice(BigDecimal.valueOf(142));
            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }
}
