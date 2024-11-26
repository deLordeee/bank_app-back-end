package com.example.Banking.App.service;

import com.example.Banking.App.entity.Loan;
import org.springframework.stereotype.Service;

import java.util.List;


public interface LoanService {
    Loan takeLoan(Long id , double amount , int years);
    List<Loan> getAllLoans();

    Loan payLoan(Long loan_id , double amount);

    List<Loan> getLoansByAccountId(Long id);
}
