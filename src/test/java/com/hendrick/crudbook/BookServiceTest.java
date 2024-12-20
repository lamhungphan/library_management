package com.hendrick.crudbook;

import com.hendrick.crudbook.entity.Book;
import com.hendrick.crudbook.repository.BookRepository;
import com.hendrick.crudbook.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    public BookServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        // Mock dữ liệu trả về
        List<Book> mockBooks = Arrays.asList(
                new Book(1L, "Book 1", "Author 1", 5),
                new Book(2L, "Book 2", "Author 2", 7)
        );
        when(bookRepository.findAll()).thenReturn(mockBooks);

        // Gọi phương thức cần test
        List<Book> books = bookService.getAllBooks();

        // Kiểm tra kết quả
        assertEquals(2, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
        verify(bookRepository, times(1)).findAll(); // Xác minh repository được gọi đúng 1 lần
    }

    @Test
    void testBorrowBookSuccess() {
        Book book = new Book(1L, "Book 1", "Author 1", 5);
        when(bookRepository.findByTitle("Book 1")).thenReturn(book);

        String result = bookService.borrowBook("Book 1");

        assertEquals("Bạn đã mượn sách thành công!", result); // trả đúng kết quả với BookService (tiếng việt)
        assertEquals(4, book.getQuantity());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testBorrowBookOutOfStock() {
        Book book = new Book(1L, "Book 1", "Author 1", 0);
        when(bookRepository.findByTitle("Book 1")).thenReturn(book);

        String result = bookService.borrowBook("Book 1");

        assertEquals("Sách không còn hoặc không tìm thấy!", result);
        verify(bookRepository, never()).save(book);
    }
}
