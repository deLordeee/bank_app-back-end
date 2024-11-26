package com.example.Banking.App.repository;

import com.example.Banking.App.entity.Deposit;
import com.example.Banking.App.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
    List<Deposit> findByDepositerAccount_Id  (Long accountId);
}
