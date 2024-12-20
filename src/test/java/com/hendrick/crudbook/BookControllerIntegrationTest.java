package com.hendrick.crudbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hendrick.crudbook.entity.Book;
import com.hendrick.crudbook.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        bookRepository.save(new Book(null, "Book 1", "Author 1", 5));
        bookRepository.save(new Book(null, "Book 2", "Author 2", 3));
    }

    @Test
    void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book 1"));
    }

    @Test
    void testAddBook() throws Exception {
        Book newBook = new Book(null, "Book 3", "Author 3", 7);

        mockMvc.perform(post("/api/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book 3"));

        // Kiểm tra cơ sở dữ liệu
        assertEquals(3, bookRepository.findAll().size());
    }

    @Test
    void testBorrowBook() throws Exception {
        mockMvc.perform(put("/api/books/borrow")
                        .param("title", "Book 1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book borrowed successfully"));

        Book book = bookRepository.findByTitle("Book 1");
        assertEquals(4, book.getQuantity());
    }
}
