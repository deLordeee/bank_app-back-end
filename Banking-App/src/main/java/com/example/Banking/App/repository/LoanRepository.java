package com.example.Banking.App.repository;

import com.example.Banking.App.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long>  {

}
