package com.hendrick.crudbook.controller;

import com.hendrick.crudbook.entity.Book;
import com.hendrick.crudbook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")  // Đảm bảo frontend có thể gửi yêu cầu
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping("/borrow/{title}")
    public ResponseEntity<String> borrowBook(@PathVariable String title) {
        String response = bookService.borrowBook(title);
        if (response.contains("Không còn")) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // Trả về lỗi nếu sách không còn
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/return/{title}")
    public ResponseEntity<String> returnBook(@PathVariable String title) {
        String response = bookService.returnBook(title);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
