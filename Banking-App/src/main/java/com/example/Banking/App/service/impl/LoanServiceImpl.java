package com.example.Banking.App.service.impl;

import com.example.Banking.App.entity.Account;
import com.example.Banking.App.entity.Loan;
import com.example.Banking.App.repository.AccountRepository;
import com.example.Banking.App.repository.LoanRepository;
import com.example.Banking.App.service.LoanService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.example.Banking.App.entity.Loan_Status.ACTIVE;
import static com.example.Banking.App.entity.Loan_Status.PAID;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    private final AccountRepository accountRepository;

    public LoanServiceImpl(LoanRepository loanRepository, AccountRepository accountRepository) {
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Loan> getAllLoans(){
        List<Loan> loans = loanRepository.findAll();
        return  loans;
    }

    @Override
    public Loan payLoan( Long loan_id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount!");
        }

        Loan loan = loanRepository
                .findById(loan_id)
                .orElseThrow(()-> new RuntimeException("Loan does not exist"));
        double total_amount;
        if(amount > loan.getRemainToPay()){
           amount = loan.getRemainToPay();

        }
        total_amount = loan.getRemainToPay() - amount;
        if(total_amount == 0){
            loan.setStatus(PAID);
        }
        loan.setRemainToPay(total_amount);


        Loan savedLoan = loanRepository.save(loan);

        return savedLoan;
    }

    @Override
    public Loan takeLoan(Long id, double amount , int years) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount!");
        }
        if (years <= 0) {
            throw new IllegalArgumentException("Invalid amount!");
        }
        Account borrower = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));

        double interestRate = 0.07;


        // Calculate monthly payment before constructing Loan
        Loan tempLoan = new Loan(); // Temporary Loan instance to access calcMonthlyPayment method
        double monthlyPayment = tempLoan.calcMonthlyPayment(amount, interestRate, years);

        Loan loan = new Loan(null, amount, amount, interestRate, years, monthlyPayment,
                LocalDate.now(), LocalDate.now().plusYears(years), borrower, ACTIVE);

        return loanRepository.save(loan);
    }
}
