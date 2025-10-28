package com.biblioteca.biblioteca_api.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.biblioteca_api.api.domain.model.Book;
import com.biblioteca.biblioteca_api.api.domain.repository.BookRepository;
import com.biblioteca.biblioteca_api.api.exception.BusinessException;
import com.biblioteca.biblioteca_api.api.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    
    private final BookRepository bookRepository;
    
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
    
    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ISBN: " + isbn));
    }
    
    public List<Book> findByCategory(String category) {
        return bookRepository.findByCategory(category);
    }
    
    public List<Book> findAvailableBooks() {
        return bookRepository.findByAvailableQuantityGreaterThan(0);
    }
    
    @Transactional
    public Book create(Book book) {
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new BusinessException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        
        if (book.getAvailableQuantity() == null) {
            book.setAvailableQuantity(book.getQuantity());
        }
        
        return bookRepository.save(book);
    }
    
    @Transactional
    public Book update(Long id, Book bookDetails) {
        Book book = findById(id);
        
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setCategory(bookDetails.getCategory());
        book.setQuantity(bookDetails.getQuantity());
        book.setDescription(bookDetails.getDescription());
        
        return bookRepository.save(book);
    }
    
    @Transactional
    public void delete(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }
    
    @Transactional
    public void decreaseAvailableQuantity(Long bookId) {
        Book book = findById(bookId);
        if (book.getAvailableQuantity() <= 0) {
            throw new BusinessException("Book is not available for loan");
        }
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);
    }
    
    @Transactional
    public void increaseAvailableQuantity(Long bookId) {
        Book book = findById(bookId);
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);
    }
}
