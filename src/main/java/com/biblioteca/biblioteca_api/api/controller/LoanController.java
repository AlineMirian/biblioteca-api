package com.biblioteca.biblioteca_api.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.biblioteca_api.api.domain.model.Loan;
import com.biblioteca.biblioteca_api.api.service.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Tag(name = "Loans", description = "Endpoints for managing book loans")
public class LoanController {
    
    private final LoanService loanService;
    
    @GetMapping
    @Operation(summary = "Get all loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.findAll());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get loan by ID")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.findById(id));
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get loans by user ID")
    public ResponseEntity<List<Loan>> getLoansByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.findByUserId(userId));
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get all active loans")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        return ResponseEntity.ok(loanService.findActiveLoans());
    }
    
    @GetMapping("/late")
    @Operation(summary = "Get all late loans")
    public ResponseEntity<List<Loan>> getLateLoans() {
        return ResponseEntity.ok(loanService.findLateLoans());
    }
    
    @PostMapping
    @Operation(summary = "Create a new loan")
    public ResponseEntity<Loan> createLoan(
            @RequestParam Long bookId,
            @RequestParam Long userId,
            @RequestParam(required = false) Integer loanDays) {
        Loan createdLoan = loanService.createLoan(bookId, userId, loanDays);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
    }
    
    @PutMapping("/{id}/return")
    @Operation(summary = "Return a book")
    public ResponseEntity<Loan> returnBook(@PathVariable Long id) {
        Loan returnedLoan = loanService.returnBook(id);
        return ResponseEntity.ok(returnedLoan);
    }
    
    @PutMapping("/{id}/renew")
    @Operation(summary = "Renew a loan")
    public ResponseEntity<Loan> renewLoan(
            @PathVariable Long id,
            @RequestParam(required = false) Integer additionalDays) {
        Loan renewedLoan = loanService.renewLoan(id, additionalDays);
        return ResponseEntity.ok(renewedLoan);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a loan")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
