package com.example.Banking.App.controller;

import com.example.Banking.App.entity.Loan;
import com.example.Banking.App.service.AccountService;
import com.example.Banking.App.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;


    @PutMapping("/{id}/takeLoan")
    public ResponseEntity<Loan> takeLoan(@PathVariable Long id , @RequestBody Map<String , Double> request){
        double amount = request.get("amount");
        double years = request.get("loan_duration");
        Loan loan = loanService.takeLoan(id , amount , (int) years);
        return ResponseEntity.ok(loan);
    }
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans(){
        List<Loan> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }
    @PutMapping("/{id}/payLoan")
    public ResponseEntity<Loan> payLoan(@PathVariable Long id ,@RequestBody Map<String , Double> request){
        double amount = request.get("amount");
        Loan loan = loanService.payLoan(id , amount);
        return  ResponseEntity.ok(loan);
    }
}
