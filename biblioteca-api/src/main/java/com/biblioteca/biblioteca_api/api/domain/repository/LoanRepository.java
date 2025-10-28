package com.biblioteca.biblioteca_api.api.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.biblioteca_api.api.domain.model.Loan;
import com.biblioteca.biblioteca_api.api.domain.model.LoanStatus;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    List<Loan> findByUserId(Long userId);
    
    List<Loan> findByBookId(Long bookId);
    
    List<Loan> findByStatus(LoanStatus status);
    
    List<Loan> findByUserIdAndStatus(Long userId, LoanStatus status);
    
    List<Loan> findByExpectedReturnDateBeforeAndStatus(LocalDate date, LoanStatus status);
}
