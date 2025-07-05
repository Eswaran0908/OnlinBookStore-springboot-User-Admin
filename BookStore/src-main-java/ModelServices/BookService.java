package com.example.demo.ModelServicesFinal;

import com.example.demo.ModelEntitiesFinal.Book;
import com.example.demo.ModelRepositoriesFinal.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

   // Add a new book
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
    

    // Get available books (for the home page)
    public Page<Book> getAvailableBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    // Edit an existing book
    public Book editBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setName(bookDetails.getName());
        book.setImageURL(bookDetails.getImageURL());
        book.setPrice(bookDetails.getPrice());
        return bookRepository.save(book);
    }

    // Delete a book by ID
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Page<Book> findAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
/*
    public Book myBooks(Long id,String name, String imageURL, String description, double price, String authorName) {
        Book book = new Book(id,name, imageURL, description, price, authorName);
        return bookRepository.save(book);
    }
*/
    public Book findById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // Delete a book by ID in the service class
    public boolean deleteBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            return true;
        }
        return false;
    }

    public List<Book> showWithoutPagination(String query, double minPrice, double maxPrice, String authorName) {
        return bookRepository.findBooks(query, minPrice, maxPrice, authorName);
    }

    public Page<Book> searchBooks(String query, double minPrice, double maxPrice, String authorName, Pageable pageable) {
        return bookRepository.findBooksByFilters(query, minPrice, maxPrice, authorName, pageable);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll(); // This will return all books
    }
    
    
    
}
