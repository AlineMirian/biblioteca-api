package com.biblioteca.biblioteca_api.api.service;

import com.biblioteca.biblioteca_api.api.domain.model.Book;
import com.biblioteca.biblioteca_api.api.domain.model.Loan;
import com.biblioteca.biblioteca_api.api.domain.model.LoanStatus;
import com.biblioteca.biblioteca_api.api.domain.model.User;
import com.biblioteca.biblioteca_api.api.domain.repository.LoanRepository;
import com.biblioteca.biblioteca_api.api.exception.BusinessException;
import com.biblioteca.biblioteca_api.api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    
    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final UserService userService;
    
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }
    
    public Loan findById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
    }
    
    public List<Loan> findByUserId(Long userId) {
        return loanRepository.findByUserId(userId);
    }
    
    public List<Loan> findActiveLoans() {
        return loanRepository.findByStatus(LoanStatus.ACTIVE);
    }
    
    public List<Loan> findLateLoans() {
        return loanRepository.findByExpectedReturnDateBeforeAndStatus(
                LocalDate.now(), LoanStatus.ACTIVE);
    }
    
    @Transactional
    public Loan createLoan(Long bookId, Long userId, Integer loanDays) {
        Book book = bookService.findById(bookId);
        User user = userService.findById(userId);
        
        if (!user.getActive()) {
            throw new BusinessException("User is not active");
        }
        
        if (book.getAvailableQuantity() <= 0) {
            throw new BusinessException("Book is not available for loan");
        }
        
        // Verificar se usuário já tem esse livro emprestado
        List<Loan> activeLoans = loanRepository.findByUserIdAndStatus(userId, LoanStatus.ACTIVE);
        boolean hasBookLoaned = activeLoans.stream()
                .anyMatch(loan -> loan.getBook().getId().equals(bookId));
        
        if (hasBookLoaned) {
            throw new BusinessException("User already has this book on loan");
        }
        
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setExpectedReturnDate(LocalDate.now().plusDays(loanDays != null ? loanDays : 14));
        loan.setStatus(LoanStatus.ACTIVE);
        
        // Diminuir quantidade disponível do livro
        bookService.decreaseAvailableQuantity(bookId);
        
        return loanRepository.save(loan);
    }
    
    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = findById(loanId);
        
        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new BusinessException("This loan is not active");
        }
        
        loan.setActualReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);
        
        // Aumentar quantidade disponível do livro
        bookService.increaseAvailableQuantity(loan.getBook().getId());
        
        return loanRepository.save(loan);
    }
    
    @Transactional
    public Loan renewLoan(Long loanId, Integer additionalDays) {
        Loan loan = findById(loanId);
        
        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new BusinessException("Only active loans can be renewed");
        }
        
        if (LocalDate.now().isAfter(loan.getExpectedReturnDate())) {
            throw new BusinessException("Cannot renew late loans");
        }
        
        loan.setExpectedReturnDate(
                loan.getExpectedReturnDate().plusDays(additionalDays != null ? additionalDays : 7));
        
        return loanRepository.save(loan);
    }
    
    @Transactional
    public void delete(Long id) {
        Loan loan = findById(id);
        
        if (loan.getStatus() == LoanStatus.ACTIVE) {
            // Se estiver ativo, devolver o livro antes de deletar
            bookService.increaseAvailableQuantity(loan.getBook().getId());
        }
        
        loanRepository.delete(loan);
    }
}
