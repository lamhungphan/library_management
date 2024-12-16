package com.hendrick.crudbook.service;

import com.hendrick.crudbook.entity.Book;
import com.hendrick.crudbook.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public String borrowBook(String title) {
        Book book = bookRepository.findByTitle(title);
        if (book != null && book.getQuantity() > 0) {
            book.setQuantity(book.getQuantity() - 1);
            bookRepository.save(book);
            return "Bạn đã mượn sách thành công!";
        } else {
            return "Sách không còn hoặc không tìm thấy!";
        }
    }

    public String returnBook(String title) {
        try{
            Book book = bookRepository.findByTitle(title);
            book.setQuantity(book.getQuantity() + 1);
            bookRepository.save(book);
            return "Bạn đã trả sách thành công ^^";
        } catch (Exception e) {
            throw new RuntimeException("Không thể trả sách" + e);
        }
    }

}
