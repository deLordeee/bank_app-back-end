package com.example.Banking.App.repository;

import com.example.Banking.App.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long>  {

    List<Loan> findByBorrowerAccount_Id(Long accountId);
}
